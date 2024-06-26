package com.example.monman;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CatatanAdapter extends RecyclerView.Adapter<CatatanData> {
    private Context context;

    private ArrayList<String> id_catatan, id_user, role_keluarga, kode_keluarga, tipe, deskripsi, jumlah, tanggal, nama_pengguna;

    public CatatanAdapter(Context context, ArrayList<String> id_catatan, ArrayList<String> id_user, ArrayList<String> role_keluarga, ArrayList<String> kode_keluarga, ArrayList<String> tipe, ArrayList<String> deskripsi, ArrayList<String> jumlah, ArrayList<String> tanggal, ArrayList<String> nama_pengguna) {
        this.context = context;
        this.id_catatan = id_catatan;
        this.id_user = id_user;
        this.role_keluarga = role_keluarga;
        this.kode_keluarga = kode_keluarga;
        this.tipe = tipe;
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.nama_pengguna = nama_pengguna;
    }

    @NonNull
    @Override
    public CatatanData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_catatan,parent,false);
        return new CatatanData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatatanData holder, int position) {
        holder.id_catatan = String.valueOf(id_catatan.get(position));
        holder.id_user = String.valueOf(id_user.get(position));
        holder.kode_keluarga = String.valueOf(kode_keluarga.get(position));
        holder.role_keluarga.setText(String.valueOf(role_keluarga.get(position)));
        holder.nama_pengguna.setText(String.valueOf(nama_pengguna.get(position)));
        holder.tipe_catatan.setText(String.valueOf(tipe.get(position)));
        holder.deskripsi.setText(String.valueOf(deskripsi.get(position)));
        holder.jumlah.setText(String.valueOf(jumlah.get(position)));
        holder.tanggal.setText(String.valueOf(tanggal.get(position)));

        final String catatan_id = String.valueOf(id_catatan.get(position));
        final String user_id = String.valueOf(id_user.get(position));
        final String kodeKeluarga = String.valueOf(kode_keluarga.get(position));
        final String roleKeluarga = String.valueOf(role_keluarga.get(position));
        final String namaPengguna = String.valueOf(nama_pengguna.get(position));
        final String tipeCatatan = String.valueOf(tipe.get(position));
        final String desk = String.valueOf(deskripsi.get(position));
        final String total = String.valueOf(jumlah.get(position));
        final String dateCat = String.valueOf(tanggal.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditCatatan.class);
                intent.putExtra("id_catatan", String.valueOf(catatan_id));
                intent.putExtra("id_user", String.valueOf(user_id));
                intent.putExtra("kode_keluarga", String.valueOf(kodeKeluarga));
                intent.putExtra("nama", namaPengguna);
                intent.putExtra("role", roleKeluarga);
                intent.putExtra("tipe", tipeCatatan);
                intent.putExtra("deskripsi", desk);
                intent.putExtra("total", total);
                intent.putExtra("tanggal", dateCat);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return id_catatan.size();
    }
}
