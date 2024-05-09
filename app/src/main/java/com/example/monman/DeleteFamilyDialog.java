package com.example.monman;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class DeleteFamilyDialog extends Dialog implements View.OnClickListener {

    private String userId, username, familyRole, familyCode;

    private DatabaseHelper db;

    public DeleteFamilyDialog(@NonNull Context context, String userId, String username, String familyRole, String familyCode) {
        super(context);
        this.userId = userId;
        this.username = username;
        this.familyRole = familyRole;
        this.familyCode = familyCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_delete_family_dialog);
        Button yesButton = findViewById(R.id.yesButton);
        Button noButton = findViewById(R.id.noButton);
        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);
        db = new DatabaseHelper(getContext());

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.yesButton) {
            boolean deleteKeluarga = db.deleteKeluarga(familyCode);
            int resetKeluarga = db.resetKeluargaUser(familyCode);
            if(deleteKeluarga && resetKeluarga != -1){
                Toast.makeText(getContext(), "Delete Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("userId",String.valueOf(userId));
                intent.putExtra("username", String.valueOf(username));
                intent.putExtra("familyRole", "Pribadi");
                intent.putExtra("familyCode","0");
                intent.putExtra("directFamilyFragment", true);
                getContext().startActivity(intent);
                dismiss();
            }
        } else if (id == R.id.noButton) {
            dismiss();
        }
    }
}