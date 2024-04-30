package com.example.thrive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class loginUser extends AppCompatActivity {

    private TextView toRegister;

    private Button btnLogin;

    private EditText nomorUser, passwordUser;

    private String nomorHp, pw;

    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        db = new DatabaseHelper(this);
//        getUserCheck();

        toRegister = findViewById(R.id.toRegister);
        btnLogin = findViewById(R.id.loginButton);

        nomorUser = findViewById(R.id.inputPhone);
        passwordUser = findViewById(R.id.inputPassword);

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginUser.this, register_user.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomorHp = nomorUser.getText().toString(); // Retrieve the phone number value here
                pw = passwordUser.getText().toString(); // Retrieve the password value here

                Cursor cursor = db.login(nomorHp,pw);

                if (cursor != null && cursor.moveToFirst()) {
                    int userId = cursor.getInt(0); // Use getColumnIndex to retrieve column index
                    String username = cursor.getString(1);
                    String familyRole = cursor.getString(4);
                    int inFamily = cursor.getInt(3);
//                    Toast.makeText(loginUser.this, username, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(loginUser.this, MainActivity.class);

                    intent.putExtra("userId", String.valueOf(userId));
                    intent.putExtra("username", username);
                    intent.putExtra("familyRole", familyRole);
                    intent.putExtra("inFamily", String.valueOf(inFamily));

                    startActivity(intent);

                    cursor.close();
                    finish();
                } else {
                    Toast.makeText(loginUser.this, "Nomor atau Password Salah", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void getUserCheck() {
        Cursor cursor = db.getUserData();

        if (cursor != null && cursor.moveToFirst()) {

            int userId = cursor.getInt(0);
            String username = cursor.getString(1);
            String familyRole = cursor.getString(5);
            int inFamily = cursor.getInt(4);

//            Toast.makeText(loginUser.this, String.valueOf(userId),Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(loginUser.this, MainActivity.class);

            intent.putExtra("userId", String.valueOf(userId));
            intent.putExtra("username", username);
            intent.putExtra("familyRole", familyRole);
            intent.putExtra("inFamily", String.valueOf(inFamily));

            startActivity(intent);

            cursor.close();
            finish();
        } else {

        }
    }

}