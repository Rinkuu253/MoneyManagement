package com.example.thrive;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerCatatanKeluarga extends RecyclerView.ViewHolder {

    String id_catatan, id_user, kode_keluarga;
    TextView tipe, deskripsi, total, tanggal, family_role, nama_pengguna;
    LinearLayout mainLayout;

    public RecyclerCatatanKeluarga(@NonNull View itemView) {
        super(itemView);
        id_catatan = String.valueOf(itemView);
        id_user = String.valueOf(itemView);
        kode_keluarga = String.valueOf(itemView);
        tipe = itemView.findViewById(R.id.tipeCatatan);
        deskripsi = itemView.findViewById(R.id.deskripsiCatatan);
        family_role = itemView.findViewById(R.id.roleKeluarga);
        nama_pengguna = itemView.findViewById(R.id.pencatatKeluarga);
        total = itemView.findViewById(R.id.jumlahCatatan);
        tanggal = itemView.findViewById(R.id.dateCatatam);
        mainLayout = itemView.findViewById(R.id.itemCatatanKeluarga);
    }
}
