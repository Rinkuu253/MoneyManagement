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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class create_family extends AppCompatActivity {

    private Button createBtn;

    private Spinner roleSpinner;

    private EditText inputName;
    private DatabaseHelper db;
    private String idUser, familyRole, inFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_family);
        db = new DatabaseHelper(this);
        createBtn = findViewById(R.id.createFamilyBtn);
        roleSpinner = findViewById(R.id.inputFamilyRole);
        inputName = findViewById(R.id.inputFamilyName);
        ImageView backButton = findViewById(R.id.backButton);
        String[] listRole = new String[]{"Ayah", "Ibu", "Anak"};

        idUser = getIntent().getStringExtra("userId");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, listRole);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        roleSpinner.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(create_family.this, family_code.class);
                intent.putExtra("userId", String.valueOf(idUser));
                startActivity(intent);
                finish();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaKeluarga = inputName.getText().toString();
                String kodeKeluarga = String.format("%04d", Integer.parseInt(idUser));
                long tambah = db.addKeluarga(idUser, namaKeluarga, kodeKeluarga);

                if(tambah != -1){
                    int update = db.updateUserFamily(idUser,kodeKeluarga, familyRole);
                    if(update != -1){
                        Intent intent = new Intent(create_family.this, pencatatan_keuangan.class);
                        intent.putExtra("userId", String.valueOf(idUser));
                        intent.putExtra("familyRole", String.valueOf(familyRole));
                        intent.putExtra("inFamily", kodeKeluarga);
                        startActivity(intent);
                        finish();
                        Toast.makeText(create_family.this, "Family Created", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(create_family.this, "Failed to Update User Family", Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(wallet_activity.this, typeCatatan + " " + idUser, Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(create_family.this, "Failed to add Family", Toast.LENGTH_SHORT).show();
                }
            }
        });

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                familyRole = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
}