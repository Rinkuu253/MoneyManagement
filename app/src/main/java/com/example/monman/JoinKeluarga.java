package com.example.monman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class JoinKeluarga extends AppCompatActivity {

    private TextView namaKeluarga;
    private DatabaseHelper db;
    private String userId, username, familyRole, familyCode, familyName, roleKeluarga;
    private Spinner roleSpinnerJoin;
    private Button joinFamilyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_keluarga);
        namaKeluarga = findViewById(R.id.namaKeluargaJoin);
        roleSpinnerJoin = findViewById(R.id.inputFamilyRole);
        joinFamilyButton = findViewById(R.id.joinFamilyBtn);
        db = new DatabaseHelper(this);

        getSetIntentData();

        String[] listRole = new String[]{"Ayah", "Ibu", "Anak"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, listRole);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        roleSpinnerJoin.setAdapter(adapter);

        roleSpinnerJoin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roleKeluarga = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        joinFamilyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int updateFamily = db.updateUserFamily(userId,String.valueOf(familyCode), roleKeluarga);
                if(updateFamily != -1){
                    Intent intent = new Intent(JoinKeluarga.this, MainActivity.class);
                    intent.putExtra("userId",String.valueOf(userId));
                    intent.putExtra("username", String.valueOf(username));
                    intent.putExtra("familyRole", roleKeluarga);
                    intent.putExtra("familyCode",String.valueOf(familyCode));
                    intent.putExtra("directFamilyFragment", true);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(JoinKeluarga.this, "Failed to Update User Family", Toast.LENGTH_SHORT).show();
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
            familyName = getIntent().getStringExtra("familyName");

            namaKeluarga.setText(familyName + "'s Family");

//            Toast.makeText(getApplicationContext(), username, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
    }
}