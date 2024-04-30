package com.example.thrive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

public class editwallet extends AppCompatActivity {

    private TextView titlePage;
    private Spinner catatanSpinner;
    private Button btnEdit;
    private DatabaseHelper db;
    private String typeCatatan, idCatatan, deskripsi, total, tipe, idUser;
    private EditText amount, desc;

    private final String [] items = new String[]{"Pengeluaran", "Pemasukan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editwallet);

        ImageView backButton = findViewById(R.id.backButton);
        ImageView deleteButton = findViewById(R.id.deleteData);
        catatanSpinner = findViewById(R.id.dropdownCatatan);
        btnEdit = findViewById(R.id.tambahButton);
        db = new DatabaseHelper(this);
        amount = findViewById(R.id.totalAmount);
        desc = findViewById(R.id.inputDeskripsi);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, items);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        catatanSpinner.setAdapter(adapter);

        getSetIntentData();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editwallet.this, pencatatan_keuangan.class);
                startActivity(intent);
                finish();
            }
        });

        catatanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeCatatan = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jumlah = amount.getText().toString();
                String deskripsi = desc.getText().toString();

                // Parsing the amount using BigDecimal
                BigDecimal parsedJumlah = null;
                try {
                    parsedJumlah = new BigDecimal(jumlah);
                } catch (NumberFormatException e) {
                    // Parsing error, handle accordingly (e.g., show error message)
                    Toast.makeText(editwallet.this, "Invalid amount format", Toast.LENGTH_SHORT).show();
                    return;
                }

                long edit = db.updateCatatan(idCatatan, typeCatatan, parsedJumlah, deskripsi);

                if(edit != -1){

                    Intent intent = new Intent(editwallet.this, pencatatan_keuangan.class);
                    intent.putExtra("userId", String.valueOf(idUser));
                    startActivity(intent);
                    finish();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idCatatan != null && !idCatatan.isEmpty()) {
                    try {
                        boolean recordDeleteBw = db.deleteCatatan(idCatatan);
                        if (recordDeleteBw) {
                            Toast.makeText(getApplicationContext(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(editwallet.this, pencatatan_keuangan.class);
                            intent.putExtra("userId", String.valueOf(idUser));
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Data Gagal Dihapus", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Invalid Catatan ID", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Catatan ID is Null or Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getSetIntentData() {
        if (getIntent().hasExtra("id_catatan")
                && getIntent().hasExtra("tipe")
                && getIntent().hasExtra("deskripsi")
                && getIntent().hasExtra("total")) {

            idCatatan = getIntent().getStringExtra("id_catatan");
            idUser = getIntent().getStringExtra("id_user");
            tipe = getIntent().getStringExtra("tipe");
            deskripsi = getIntent().getStringExtra("deskripsi");
            total = getIntent().getStringExtra("total");

            amount.setText(total);
            desc.setText(deskripsi);

            if(Objects.equals(tipe, "Pengeluaran")){
                catatanSpinner.setSelection(0);
            } else{
                catatanSpinner.setSelection(1);
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
    }
}