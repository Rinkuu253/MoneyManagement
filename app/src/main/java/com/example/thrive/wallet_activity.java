package com.example.thrive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class wallet_activity extends AppCompatActivity {

    private Spinner catatanSpinner;
    private Button btnTambah;
    private DatabaseHelper db;
    private String typeCatatan;
    private EditText amount, desc;

    private String idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        ImageView backButton = findViewById(R.id.backButton);
        catatanSpinner = findViewById(R.id.dropdownCatatan);
        btnTambah = findViewById(R.id.tambahButton);
        db = new DatabaseHelper(this);
        amount = findViewById(R.id.totalAmount);
        desc = findViewById(R.id.inputDeskripsi);
        String [] items = new String[]{"Pengeluaran", "Pemasukan"};
        String [] jenisPengeluaran = new String[]{"Jajan", "Transportasi", "Kebutuhan"};
        String [] jenisPemasukan = new String[]{"Gaji", "Donasi", "THR"};

        idUser = getIntent().getStringExtra("userId");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, items);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        catatanSpinner.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(wallet_activity.this, pencatatan_keuangan.class);
                intent.putExtra("userId", String.valueOf(idUser));
                startActivity(intent);
                finish();
            }
        });
//        Toast.makeText(this, String.valueOf(typeCatatan), Toast.LENGTH_SHORT).show();

        catatanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeCatatan = (String) parent.getItemAtPosition(position);

//                if(typeCatatan == "Pengeluaran"){
//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, items);
//                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
//                    catatanSpinner.setAdapter(adapter);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inside your activity or wherever you need to get today's date
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String todayDate = dateFormat.format(calendar.getTime());
                String jumlah = amount.getText().toString();
                String deskripsi = desc.getText().toString();

                // Parsing the amount using BigDecimal
                BigDecimal parsedJumlah = null;
                try {
                    parsedJumlah = new BigDecimal(jumlah);
                } catch (NumberFormatException e) {
                    // Parsing error, handle accordingly (e.g., show error message)
                    Toast.makeText(wallet_activity.this, "Invalid amount format", Toast.LENGTH_SHORT).show();
                    return;
                }

                long tambah = db.addCatatan(idUser, typeCatatan, parsedJumlah, deskripsi, todayDate);

                if(tambah != -1){
//                    Toast.makeText(wallet_activity.this, typeCatatan + " " + idUser, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(wallet_activity.this, pencatatan_keuangan.class);
                    intent.putExtra("userId", String.valueOf(idUser));
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}