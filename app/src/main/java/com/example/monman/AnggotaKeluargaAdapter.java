package com.example.monman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnggotaKeluargaAdapter extends RecyclerView.Adapter<AnggotaKeluargaData> {

    Context context;

    ArrayList<String> kodeUser, kodeKeluarga, namaAnggota, roleAnggota;

    private String loggedInUserRole; // Add this variable

    public AnggotaKeluargaAdapter(Context context, ArrayList<String> kodeUser, ArrayList<String> kodeKeluarga, ArrayList<String> namaAnggota, ArrayList<String> roleAnggota, String loggedInUserRole) {
        this.context = context;
        this.kodeUser = kodeUser;
        this.kodeKeluarga = kodeKeluarga;
        this.namaAnggota = namaAnggota;
        this.roleAnggota = roleAnggota;
        this.loggedInUserRole = loggedInUserRole; // Store the logged-in user role
    }

    @NonNull
    @Override
    public AnggotaKeluargaData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_keluarga,parent,false);
        return new AnggotaKeluargaData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnggotaKeluargaData holder, int position) {
        holder.kodeUser = String.valueOf(kodeUser.get(position));
        holder.kodeKeluarga = String.valueOf(kodeKeluarga.get(position));
        holder.namaAnggota.setText(String.valueOf(namaAnggota.get(position)));
        holder.roleAnggota.setText(String.valueOf(roleAnggota.get(position)));

        final String userId = String.valueOf(kodeUser.get(position));
        final String familyCode = String.valueOf(kodeKeluarga.get(position));
        final String familyRole = roleAnggota.get(position);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loggedInUserRole.equals("Anak")) {
                    if(familyRole.equals("Anak")){
                        CatatanAnggotaDialog dialog = new CatatanAnggotaDialog(context, familyCode, userId);
                        dialog.show();
                    } else{
                        Toast.makeText(context, "Anda Tidak Memiliki Akses", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    CatatanAnggotaDialog dialog = new CatatanAnggotaDialog(context, familyCode, userId);
                    dialog.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return kodeUser.size();
    }
}
