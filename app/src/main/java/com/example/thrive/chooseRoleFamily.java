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

public class chooseRoleFamily extends AppCompatActivity {

    private Button roleBtn;

    private Spinner roleSpinner;
    private DatabaseHelper db;
    private String idUser, familyRole, inFamily, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role_family);

        db = new DatabaseHelper(this);
        roleBtn = findViewById(R.id.createFamilyBtn);
        roleSpinner = findViewById(R.id.inputFamilyRole);
        ImageView backButton = findViewById(R.id.backButton);
        String[] listRole = new String[]{"Ayah", "Ibu", "Anak"};

        idUser = getIntent().getStringExtra("userId");
        inFamily = getIntent().getStringExtra("inFamily");
        username = getIntent().getStringExtra("username");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, listRole);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        roleSpinner.setAdapter(adapter);

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

        roleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int role = db.updateFamilyforUser(idUser, familyRole, inFamily);

                    if(role != -1){
                        Toast.makeText(chooseRoleFamily.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(chooseRoleFamily.this, pencatatan_keuangan_keluarga.class);
                        intent.putExtra("userId", String.valueOf(idUser));
                        intent.putExtra("familyRole", String.valueOf(familyRole));
                        intent.putExtra("inFamily", String.valueOf(inFamily));
                        startActivity(intent);
                        finish();
                    }
                }catch (Exception e){
                    Toast.makeText(chooseRoleFamily.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}