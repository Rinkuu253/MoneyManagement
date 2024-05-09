package com.example.monman;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CatatanData extends RecyclerView.ViewHolder {

    String id_user, id_catatan, kode_keluarga;

    TextView tipe_catatan,deskripsi,jumlah,tanggal,role_keluarga,nama_pengguna;

    LinearLayout mainLayout;


    public CatatanData(@NonNull View itemView) {
        super(itemView);
        id_catatan = String.valueOf(itemView);
        id_user = String.valueOf(itemView);
        kode_keluarga = String.valueOf(itemView);
        tipe_catatan =itemView.findViewById(R.id.tipeCatatan);
        deskripsi =itemView.findViewById(R.id.deskripsiCatatan);
        jumlah =itemView.findViewById(R.id.jumlahCatatan);
        tanggal =itemView.findViewById(R.id.dateCatatam);
        role_keluarga =itemView.findViewById(R.id.roleKeluarga);
        nama_pengguna =itemView.findViewById(R.id.namaPencatat);
        mainLayout =itemView.findViewById(R.id.itemCatatan);
    }
}
