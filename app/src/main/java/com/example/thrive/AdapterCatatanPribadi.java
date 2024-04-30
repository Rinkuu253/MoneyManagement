package com.example.thrive;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCatatanPribadi extends RecyclerView.Adapter<RecyclerCatatanPribadi> {

    private Context context;

    private ArrayList<String> id_catatan, id_user;

    private ArrayList<String> tipe, deskripsi, total, tanggal;

    public AdapterCatatanPribadi(Context context, ArrayList<String> id_catatan, ArrayList<String> id_user,ArrayList<String> tipe, ArrayList<String> deskripsi, ArrayList<String> total,ArrayList<String> tanggal ){
        this.context = context;
        this.id_catatan = id_catatan;
        this.id_user = id_user;
        this.tipe = tipe;
        this.deskripsi = deskripsi;
        this.total = total;
        this.tanggal = tanggal;
    }

    @NonNull
    @Override
    public RecyclerCatatanPribadi onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_catatan,parent,false);
        return new RecyclerCatatanPribadi(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerCatatanPribadi holder, int position) {
        holder.id_catatan = String.valueOf(id_catatan.get(position));
        holder.id_user = String.valueOf(id_user.get(position));
        holder.tipe.setText(String.valueOf(tipe.get(position)));
        holder.deskripsi.setText(String.valueOf(deskripsi.get(position)));
        holder.total.setText(String.valueOf(total.get(position)));
        holder.tanggal.setText(String.valueOf(tanggal.get(position)));

        final String catatan_id = String.valueOf(id_catatan.get(position));
        final String user_id = String.valueOf(id_user.get(position));
        final String tipeCatatan = String.valueOf(tipe.get(position));
        final String desk = String.valueOf(deskripsi.get(position));
        final String jumlah = String.valueOf(total.get(position));
        final String dateCat = String.valueOf(tanggal.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, editwallet.class);
                intent.putExtra("id_catatan", String.valueOf(catatan_id));
                intent.putExtra("id_user", String.valueOf(user_id));
                intent.putExtra("tipe", tipeCatatan);
                intent.putExtra("deskripsi", desk);
                intent.putExtra("total", jumlah);
                intent.putExtra("tanggal", dateCat);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tipe.size();
    }
}
