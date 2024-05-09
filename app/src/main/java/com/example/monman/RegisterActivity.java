package com.example.monman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameInput, phoneInput, passwordInput;

    private Button registerBtn;

    private TextView toLoginTxt;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        usernameInput = findViewById(R.id.inputUsername);
        phoneInput = findViewById(R.id.inputPhone);
        passwordInput = findViewById(R.id.inputPassword);
        registerBtn = findViewById(R.id.registerButton);
        toLoginTxt = findViewById(R.id.toLogin);

        toLoginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = usernameInput.getText().toString();
                String nomorHp = phoneInput.getText().toString();
                String passw = passwordInput.getText().toString();

                try {
                    Cursor cursor = db.getUserData(nomorHp);
                    if(cursor.getCount() > 0){
                        Toast.makeText(RegisterActivity.this, "User Already Registered", Toast.LENGTH_SHORT).show();
                    } else{
                        long insertedId = db.addUser(nama, passw, nomorHp, "Pribadi");
                        if(insertedId != -1){
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.putExtra("userId", String.valueOf(insertedId));
                            intent.putExtra("username", nama);
                            intent.putExtra("familyRole", "Pribadi");
                            intent.putExtra("familyCode","0");
                            startActivity(intent);
                            finish();
                        }
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}