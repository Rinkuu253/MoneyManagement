package com.example.thrive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class register_user extends AppCompatActivity {

    private Button btnregister;

    private DatabaseHelper db;

    private EditText username, noHp, password;

    private TextView toLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        db = new DatabaseHelper(this);
        toLogin = findViewById(R.id.toLogin);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register_user.this, loginUser.class);
                startActivity(intent);
                finish();
            }
        });

        btnregister = findViewById(R.id.registerButton);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = findViewById(R.id.inputUsername);
                noHp = findViewById(R.id.inputPhone);
                password = findViewById(R.id.inputPassword);

                String nama = username.getText().toString();
                String nomorHp = noHp.getText().toString();
                String pw = password.getText().toString();

                try {
                    long insertedId = db.addUser(nama, pw, nomorHp, "single");
                    if(insertedId != -1){
                        Intent intent = new Intent(register_user.this, MainActivity.class);
                        intent.putExtra("username", nama);
                        intent.putExtra("familyRole", "single");
                        intent.putExtra("userId", String.valueOf(insertedId));
                        intent.putExtra("inFamily","0");
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}