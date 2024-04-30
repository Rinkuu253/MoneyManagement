package com.example.thrive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView username, role;
    private String idUser, nama, familyRole, inFamily;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout menuCatatan = findViewById(R.id.menuCatatan);
        LinearLayout menuKeluarga = findViewById(R.id.menuCatatanKeluarga);
        LinearLayout menuGoals = findViewById(R.id.menuGoals);
        LinearLayout menuGrafik = findViewById(R.id.menuGraph);

        username = findViewById(R.id.userNameTopBar);
        role = findViewById(R.id.userRoleTopBar);

        getSetIntentData();

//        Toast.makeText(MainActivity.this, idUser, Toast.LENGTH_SHORT).show();

        menuCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, pencatatan_keuangan.class);
                intent.putExtra("userId", idUser);
                startActivity(intent);
            }
        });

        menuKeluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, family_code.class);
                intent.putExtra("userId", String.valueOf(idUser));
                intent.putExtra("username", String.valueOf(nama));
                intent.putExtra("familyRole", String.valueOf(familyRole));
                intent.putExtra("inFamily", String.valueOf(inFamily));
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
            inFamily = getIntent().getStringExtra("inFamily");

            username.setText(nama);
            role.setText(familyRole);

        } else {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
    }

}