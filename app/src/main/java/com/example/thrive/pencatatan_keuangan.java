package com.example.thrive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class pencatatan_keuangan extends AppCompatActivity {

    private TextView dateView, profit, loss, totalAmount;
    private Calendar currentDate;

    private RecyclerView recyclerView;
    private String idUser, bulan, tahun;
    private ArrayList<String> id_catatan,id_user, tipe, deskripsi, total, tanggal;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencatatan_keuangan);
        db = new DatabaseHelper(this);
        ImageView leftDate = findViewById(R.id.monthBefore);
        ImageView rightDate = findViewById(R.id.monthAfter);
        recyclerView = findViewById(R.id.recyclerWallet);
        dateView = findViewById(R.id.dateText);
        profit = findViewById(R.id.amountProfit);
        loss = findViewById(R.id.amountLoss);
        totalAmount = findViewById(R.id.amountTotal);
        ImageView downloading = findViewById(R.id.downloadButton);
        FloatingActionButton buttonAdd = findViewById(R.id.buttonAdd);
//        profit.setText("123123123");
        id_catatan = new ArrayList<>();
        id_user = new ArrayList<>();
        tipe = new ArrayList<>();
        deskripsi = new ArrayList<>();
        total = new ArrayList<>();
        tanggal = new ArrayList<>();

        AdapterCatatanPribadi bk = new AdapterCatatanPribadi(
                pencatatan_keuangan.this,
                id_catatan,
                id_user,
                tipe,
                deskripsi,
                total,
                tanggal
        );

        recyclerView.setAdapter(bk);
        recyclerView.setLayoutManager(new LinearLayoutManager(pencatatan_keuangan.this));
//        storeDataToArray();

        idUser = getIntent().getStringExtra("userId");

        // Get current date
        currentDate = Calendar.getInstance();

        // Display current month and year
        updateDateView();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pencatatan_keuangan.this, wallet_activity.class);
                intent.putExtra("userId", String.valueOf(idUser));
                startActivity(intent);
            }
        });

        // Handle clicks on left arrow (previous month)
        leftDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateDateView();
            }
        });

        // Handle clicks on right arrow (next month)
        rightDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateDateView();
            }
        });
    }

    private void updateDateView() {
        SimpleDateFormat dateFormatText = new SimpleDateFormat("MMMM yyyy", new Locale("id", "ID"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM yyyy", new Locale("id", "ID"));
        String formattedDate = dateFormat.format(currentDate.getTime());
        String formattedDateText = dateFormatText.format(currentDate.getTime());
        // Split the formatted date string to extract month and year
        String[] parts = formattedDate.split(" ");
        if (parts.length == 2) {
            bulan = parts[0]; // Extracted month
            tahun = parts[1]; // Extracted year
        }
        dateView.setText(formattedDateText);
        storeDataToArray();
    }

    void storeDataToArray() {
        try {

            int totalPengeluaran = 0, totalPemasukan = 0, totalSemua = 0;
            Cursor cursor = db.getCatatanByMonthAndYear(String.valueOf(bulan), String.valueOf(tahun), String.valueOf(idUser));
//            Cursor cursor = db.getCatatanByMonthAndYear("04", "2024");
            if (cursor.getCount() == 0) {
                id_catatan.clear();
                id_user.clear();
                tipe.clear();
                deskripsi.clear();
                total.clear();
                tanggal.clear();

                loss.setText("0");
                profit.setText("0");
                totalAmount.setText("0");

                // Notify the adapter that the data has changed
                recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(pencatatan_keuangan.this, "Data belum ada", Toast.LENGTH_SHORT).show();
            } else {
                // Clear the arrays before adding new data
                id_catatan.clear();
                id_user.clear();
                tipe.clear();
                deskripsi.clear();
                total.clear();
                tanggal.clear();

                while (cursor.moveToNext()) {
                    id_catatan.add(cursor.getString(0));
                    id_user.add(cursor.getString(1));
                    tipe.add(cursor.getString(2));
                    float floatTotal = Float.parseFloat(cursor.getString(3));
                    String formattedTotal = String.format("%.0f", floatTotal);
                    total.add(formattedTotal);
                    deskripsi.add(cursor.getString(4));
                    String[] dmy = cursor.getString(5).split("-");
                    tanggal.add(dmy[2]+"-"+dmy[1]+"-"+dmy[0]);

                    // Summing the totals based on the type
                    if (cursor.getString(2).equals("Pengeluaran")) {
                        totalPengeluaran += floatTotal;
                        totalSemua -= floatTotal;
                    } else if (cursor.getString(2).equals("Pemasukan")) {
                        totalPemasukan += floatTotal;
                        totalSemua += floatTotal;
//                        Toast.makeText((Context) this, (CharSequence) tipe, Toast.LENGTH_SHORT).show();


                    }
                }
                cursor.close();

                // Notify the adapter that the data has changed
                recyclerView.getAdapter().notifyDataSetChanged();

//                Toast.makeText(this, String.valueOf(totalPemasukan), Toast.LENGTH_SHORT).show();
                loss.setText(String.valueOf(totalPengeluaran));
                profit.setText(String.valueOf(totalPemasukan));
                totalAmount.setText(String.valueOf(totalSemua));
            }
        } catch (Exception e) {
            Toast.makeText(pencatatan_keuangan.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

}