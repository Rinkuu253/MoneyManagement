package com.example.monman;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChartCatatanData extends RecyclerView.ViewHolder {

    TextView deskripsiChart, totalChart;

    LinearLayout chartLayout;
    public ChartCatatanData(@NonNull View itemView) {
        super(itemView);
        deskripsiChart = itemView.findViewById(R.id.deskripsiCatatanChart);
        totalChart = itemView.findViewById(R.id.jumlahCatatanChart);
        chartLayout = itemView.findViewById(R.id.itemCatatanForChart);
    }
}
