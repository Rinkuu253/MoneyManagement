package com.example.monman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends AppCompatActivity {

    private String userId, username, familyRole, familyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView textPage = findViewById(R.id.textPage);

        textPage.setText("Profile");

        getSetIntentData();
        TextView namaUser = findViewById(R.id.usernameProfile);
        TextView roleUser = findViewById(R.id.textViewRole);
        Button logoutBtn = findViewById(R.id.buttonLogout);
        namaUser.setText(username);
        roleUser.setText(familyRole);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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