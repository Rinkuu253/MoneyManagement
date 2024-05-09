package com.example.monman;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnggotaKeluargaData extends RecyclerView.ViewHolder{

    TextView namaAnggota, roleAnggota;

    String kodeKeluarga, kodeUser;

    LinearLayout mainLayout;
    public AnggotaKeluargaData(@NonNull View itemView) {
        super(itemView);
        kodeKeluarga = String.valueOf(itemView);
        kodeUser = String.valueOf(itemView);
        namaAnggota = itemView.findViewById(R.id.namaAnggotaKeluarga);
        roleAnggota = itemView.findViewById(R.id.roleAnggotaKeluarga);
        mainLayout = itemView.findViewById(R.id.itemAnggotaKeluarga);
    }

}
