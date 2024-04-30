package com.example.thrive;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerCatatanPribadi extends RecyclerView.ViewHolder {

    String id_catatan, id_user;
    TextView tipe, deskripsi, total, tanggal;
    LinearLayout mainLayout;

    public RecyclerCatatanPribadi(@NonNull View itemView) {
        super(itemView);
        id_catatan = String.valueOf(itemView);
        tipe = itemView.findViewById(R.id.tipeCatatan);
        deskripsi = itemView.findViewById(R.id.deskripsiCatatan);
        total = itemView.findViewById(R.id.jumlahCatatan);
        tanggal = itemView.findViewById(R.id.dateCatatam);
        mainLayout = itemView.findViewById(R.id.itemCatatan);
    }
}
