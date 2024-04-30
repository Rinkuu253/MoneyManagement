package com.example.thrive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
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

public class pencatatan_keuangan_keluarga extends AppCompatActivity {

    private TextView dateView, profit, loss, totalAmount;
    private Calendar currentDate;

    private RecyclerView recyclerView;
    private String idUser, nama, familyRole,inFamily, bulan, tahun;
    private ArrayList<String> id_catatan, id_user, tipe, deskripsi, total, tanggal, family_role, kode_keluarga, nama_pengguna;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencatatan_keuangan_keluarga);
        db = new DatabaseHelper(this);
        ImageView leftDate = findViewById(R.id.monthBefore);
        ImageView rightDate = findViewById(R.id.monthAfter);
        recyclerView = findViewById(R.id.recyclerWallet);
        dateView = findViewById(R.id.dateText);
        profit = findViewById(R.id.amountProfit);
        loss = findViewById(R.id.amountLoss);
        totalAmount = findViewById(R.id.amountTotal);
        ImageView downloading = findViewById(R.id.downloadButton);
        FloatingActionButton buttonAdd = findViewById(R.id.btnTambahCatatanKeluarga);

        id_catatan = new ArrayList<>();
        id_user = new ArrayList<>();
        family_role = new ArrayList<>();
        kode_keluarga = new ArrayList<>();
        tipe = new ArrayList<>();
        deskripsi = new ArrayList<>();
        total = new ArrayList<>();
        tanggal = new ArrayList<>();
        nama_pengguna = new ArrayList<>();

        // Initialize the adapter
        AdapterCatatanKeluarga bk = new AdapterCatatanKeluarga(
                pencatatan_keuangan_keluarga.this,
                id_catatan,
                id_user,
                family_role,
                kode_keluarga,
                tipe,
                deskripsi,
                total,
                tanggal,
                nama_pengguna
        );

        recyclerView.setAdapter(bk);
        recyclerView.setLayoutManager(new LinearLayoutManager(pencatatan_keuangan_keluarga.this));
//        storeDataToArray();

        getSetIntentData();

        idUser = getIntent().getStringExtra("userId");

        // Get current date
        currentDate = Calendar.getInstance();

        // Display current month and year
        updateDateView();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pencatatan_keuangan_keluarga.this, wallet_activity.class);
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

            Cursor cursor;
            if (!String.valueOf(familyRole).equals("anak")) {
                cursor = db.getCatatanKeluargaUserAndKeluargaByMonthAndYear(String.valueOf(bulan), String.valueOf(tahun), String.valueOf(inFamily));
            } else {
                cursor = db.getCatatanKeluargaByMonthAndYearById(String.valueOf(bulan), String.valueOf(tahun), String.valueOf(inFamily), String.valueOf(idUser));
            }

            if (cursor.getCount() == 0) {
                id_catatan.clear();
                id_user.clear();
                family_role.clear();
                kode_keluarga.clear();
                tipe.clear();
                deskripsi.clear();
                total.clear();
                tanggal.clear();
                nama_pengguna.clear();

                loss.setText("0");
                profit.setText("0");
                totalAmount.setText("0");

                // Notify the adapter that the data has changed
                recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(pencatatan_keuangan_keluarga.this, "Data belum ada", Toast.LENGTH_SHORT).show();
            } else {
                // Clear the arrays before adding new data
                id_catatan.clear();
                id_user.clear();
                family_role.clear();
                kode_keluarga.clear();
                tipe.clear();
                deskripsi.clear();
                total.clear();
                tanggal.clear();
                nama_pengguna.clear();

                while (cursor.moveToNext()) {
                    nama_pengguna.add(cursor.getString(0));
                    family_role.add(cursor.getString(1));
                    kode_keluarga.add(cursor.getString(3));
                    id_catatan.add(cursor.getString(4));
                    float floatTotal = Float.parseFloat(cursor.getString(5));
                    String formattedTotal = String.format("%.0f", floatTotal);
                    total.add(formattedTotal);
                    tipe.add(cursor.getString(6));
                    deskripsi.add(cursor.getString(7));
                    String[] dmy = cursor.getString(8).split("-");
                    tanggal.add(dmy[2]+"-"+dmy[1]+"-"+dmy[0]);

                    // Summing the totals based on the type
                    if (cursor.getString(6).equals("Pengeluaran")) {
                        totalPengeluaran += floatTotal;
                        totalSemua -= floatTotal;
                    } else if (cursor.getString(6).equals("Pemasukan")) {
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
            Toast.makeText(pencatatan_keuangan_keluarga.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    void getSetIntentData() {
        if (getIntent().hasExtra("userId")
                && getIntent().hasExtra("familyRole")
                && getIntent().hasExtra("inFamily")
        ) {

            idUser = getIntent().getStringExtra("userId");
            familyRole = getIntent().getStringExtra("familyRole");
            inFamily = getIntent().getStringExtra("inFamily");

        } else {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
    }
}