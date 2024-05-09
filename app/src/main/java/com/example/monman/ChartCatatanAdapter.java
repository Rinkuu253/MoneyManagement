package com.example.monman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChartCatatanAdapter extends RecyclerView.Adapter<ChartCatatanData> {

    private Context context;

    private ArrayList<String> deskripsiChart, totalChart;

    public ChartCatatanAdapter(Context context, ArrayList<String> deskripsiChart, ArrayList<String> totalChart) {
        this.context = context;
        this.deskripsiChart = deskripsiChart;
        this.totalChart = totalChart;
    }

    @NonNull
    @Override
    public ChartCatatanData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_chart_catatan,parent,false);
        return new ChartCatatanData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartCatatanData holder, int position) {
        holder.deskripsiChart.setText(String.valueOf(deskripsiChart.get(position)));
        holder.totalChart.setText(String.valueOf(totalChart.get(position)));

        final String desk = String.valueOf(deskripsiChart.get(position));
        final String total = String.valueOf(totalChart.get(position));
    }

    @Override
    public int getItemCount() {
        return deskripsiChart.size();
    }
}
