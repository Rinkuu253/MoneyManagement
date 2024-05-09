package com.example.monman;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class KeluargaFragment extends Fragment {

    private String userId;
    private String username;
    private String familyRole;
    private String familyCode;
    private DatabaseHelper db;
    private Button toCreateFamily, toJoinFamily;

    private EditText kodeFamily;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_keluarga, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("userId");
            username = bundle.getString("username");
            familyRole = bundle.getString("familyRole");
            familyCode = bundle.getString("familyCode");
            kodeFamily = view.findViewById(R.id.inputKodeKeluarga);
            toJoinFamily = view.findViewById(R.id.joinKeluargaButton);
            toCreateFamily = view.findViewById(R.id.createKeluargaButton);
            db = new DatabaseHelper(getActivity());

            toCreateFamily.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), tambah_keluarga.class);
                    intent.putExtra("userId",String.valueOf(userId));
                    intent.putExtra("username", String.valueOf(username));
                    intent.putExtra("familyRole", familyRole);
                    intent.putExtra("familyCode",familyCode);
                    startActivity(intent);
                }
            });

            toJoinFamily.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String kodeKeluarga = kodeFamily.getText().toString();
                    Cursor cursor = db.getFamily(kodeKeluarga);

                    if(cursor.getCount()>0){
                        cursor.moveToFirst();
                        Toast.makeText(getActivity(), cursor.getString(1), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), JoinKeluarga.class);
                        intent.putExtra("userId",String.valueOf(userId));
                        intent.putExtra("username", String.valueOf(username));
                        intent.putExtra("familyRole", familyRole);
                        intent.putExtra("familyCode", String.valueOf(kodeKeluarga));
                        intent.putExtra("familyName", cursor.getString(1));
                        startActivity(intent);
                    } else{
                        Toast.makeText(getActivity(), "Family Not Found\nPlease Check Your Code Again", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        return view;
    }
}