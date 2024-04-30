package com.example.thrive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class family_code extends AppCompatActivity {

    private String familyCode, idUser, familyRole, nama;

    private EditText joinCode;

    private Button joinBtn, createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_code);

        joinBtn = findViewById(R.id.joinFamilyButton);
        createBtn = findViewById(R.id.createFamilyButton);
        joinCode = findViewById(R.id.inputCodeFamily);
        getSetIntentData();

        if(!String.valueOf(familyCode).equals("0")){
            if(!String.valueOf(familyRole).equals("single")){
                Intent intent = new Intent(this, pencatatan_keuangan.class);
                intent.putExtra("userId", String.valueOf(idUser));
                intent.putExtra("familyRole", String.valueOf(familyRole));
                intent.putExtra("inFamily", String.valueOf(familyCode));
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(family_code.this, chooseRoleFamily.class);
                intent.putExtra("userId", String.valueOf(idUser));
                intent.putExtra("inFamily", String.valueOf(familyCode));
                startActivity(intent);
                finish();
            }

        }

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kodeJoin = joinCode.getText().toString();
                Intent intent = new Intent(family_code.this, chooseRoleFamily.class);
                intent.putExtra("userId", String.valueOf(idUser));
                intent.putExtra("inFamily", kodeJoin);
                startActivity(intent);
                finish();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(family_code.this, create_family.class);
                intent.putExtra("userId", String.valueOf(idUser));
                startActivity(intent);
            }
        });
    }

    void getSetIntentData() {
        if (getIntent().hasExtra("userId")
                && getIntent().hasExtra("username")
                && getIntent().hasExtra("familyRole")
                && getIntent().hasExtra("inFamily")
        ) {

            idUser = getIntent().getStringExtra("userId");
            nama = getIntent().getStringExtra("username");
            familyRole = getIntent().getStringExtra("familyRole");
            familyCode = getIntent().getStringExtra("inFamily");

        } else {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
    }
}