package com.example.monman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class tambah_keluarga extends AppCompatActivity {

    private EditText familyName;
    private Button crtFamilyBtn;
    private DatabaseHelper db;
    private Spinner roleSpinner;

    private String userId, username, familyRole, familyCode, roleKeluarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_keluarga);

        familyName = findViewById(R.id.inputFamilyName);
        crtFamilyBtn = findViewById(R.id.createFamilyBtn);
        roleSpinner = findViewById(R.id.inputFamilyRole);
        db = new DatabaseHelper(this);

        getSetIntentData();

        String[] listRole = new String[]{"Ayah", "Ibu", "Anak"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, listRole);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        roleSpinner.setAdapter(adapter);

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roleKeluarga = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        crtFamilyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaKeluarga = familyName.getText().toString();
                long kodeKeluarga = db.addKeluarga(namaKeluarga);
                if(kodeKeluarga != -1) {
                    int updateFamily = db.updateUserFamily(userId,String.valueOf(kodeKeluarga), roleKeluarga);
                    if(updateFamily != -1){
                        Intent intent = new Intent(tambah_keluarga.this, MainActivity.class);
                        intent.putExtra("userId",String.valueOf(userId));
                        intent.putExtra("username", String.valueOf(username));
                        intent.putExtra("familyRole", roleKeluarga);
                        intent.putExtra("familyCode",String.valueOf(kodeKeluarga));
                        intent.putExtra("directFamilyFragment", true);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(tambah_keluarga.this, "Failed to Update User Family", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    void getSetIntentData() {
        if (getIntent().hasExtra("userId")
                && getIntent().hasExtra("username")
                && getIntent().hasExtra("familyRole")
                && getIntent().hasExtra("familyCode")
        ) {

            userId = getIntent().getStringExtra("userId");
            username = getIntent().getStringExtra("username");
            familyRole = getIntent().getStringExtra("familyRole");
            familyCode = getIntent().getStringExtra("familyCode");

//            Toast.makeText(getApplicationContext(), username, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
    }
}