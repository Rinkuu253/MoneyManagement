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

public class LoginActivity extends AppCompatActivity {

    private EditText phoneInput, passwordInput;

    private Button loginBtn;

    private TextView toRegisterTxt;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        phoneInput = findViewById(R.id.inputPhone);
        passwordInput = findViewById(R.id.inputPassword);
        loginBtn = findViewById(R.id.loginButton);
        toRegisterTxt = findViewById(R.id.toRegister);

        toRegisterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomorHp = phoneInput.getText().toString();
                String passw = passwordInput.getText().toString();

                Cursor cursor = db.login(nomorHp,passw);

                if (cursor != null && cursor.moveToFirst()) {
                    String userId = cursor.getString(0); // Use getColumnIndex to retrieve column index
                    String username = cursor.getString(1);
                    String familyCode = cursor.getString(3);
                    String familyRole = cursor.getString(4);
//                    Toast.makeText(loginUser.this, username, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    intent.putExtra("userId", String.valueOf(userId));
                    intent.putExtra("username", username);
                    intent.putExtra("familyRole", familyRole);
                    intent.putExtra("familyCode", String.valueOf(familyCode));

                    startActivity(intent);

                    cursor.close();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Nomor atau Password Salah", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}