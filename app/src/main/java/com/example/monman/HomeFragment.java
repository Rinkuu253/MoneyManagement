package com.example.monman;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class HomeFragment extends Fragment {

    private TextView nama, role, incomeProfit, lossLoss, todayMonthYear;

    private CardView beritaSatu, beritaDua;

    private Calendar currentDate;

    private String userId, username, familyRole, familyCode, bulan, tahun;

    PieChart pieChart;
    private DatabaseHelper db;

    private ImageView profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Retrieve data from bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
             userId = bundle.getString("userId");
             username = bundle.getString("username");
             familyRole = bundle.getString("familyRole");
             familyCode = bundle.getString("familyCode");

            nama = view.findViewById(R.id.namaUser);
            role = view.findViewById(R.id.roleUser);
            beritaSatu = view.findViewById(R.id.berita1);
            beritaDua = view.findViewById(R.id.berita2);
            pieChart = view.findViewById(R.id.piechartHome);
            incomeProfit = view.findViewById(R.id.income);
            lossLoss = view.findViewById(R.id.loss);
            todayMonthYear = view.findViewById(R.id.nowMonthYear);
            profile = view.findViewById(R.id.profilePict);
            db = new DatabaseHelper(getActivity());
            currentDate = Calendar.getInstance();
            setData();
//            Toast.makeText(getActivity(), "Welcome " + familyCode, Toast.LENGTH_SHORT).show();

            nama.setText(username);
            role.setText(familyRole);

            SimpleDateFormat dateFormatText = new SimpleDateFormat("MMMM yyyy", new Locale("id", "ID"));
            String formattedDateText = dateFormatText.format(currentDate.getTime());
            todayMonthYear.setText(formattedDateText);
            beritaSatu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://keuangan.kontan.co.id/news/rampungkan-akuisisi-oto-grup-aset-btpn-naik-jadi-rp-23984-triliun"));
                    startActivity(browserIntent);
                }
            });

            beritaDua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://keuangan.kontan.co.id/news/nasabah-valas-wajib-merapat-intip-kurs-dollar-rupiah-di-bca-hari-ini-jumat-35"));
                    startActivity(browserIntent);
                }
            });

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), UserProfile.class);
                    intent.putExtra("userId",String.valueOf(userId));
                    intent.putExtra("username", String.valueOf(username));
                    intent.putExtra("familyRole", familyRole);
                    intent.putExtra("familyCode",familyCode);
                    startActivity(intent);
                }
            });

        }

        return view;
    }
    private void setData(){
        int totalPengeluaran = 0, totalPemasukan = 0, totalSemua = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM yyyy", new Locale("id", "ID"));
        String formattedDate = dateFormat.format(currentDate.getTime());
        String[] parts = formattedDate.split(" ");
        if (parts.length == 2) {
            bulan = parts[0]; // Extracted month
            tahun = parts[1]; // Extracted year
        }
        pieChart.clearChart();

        Cursor cursor = db.getDataProfitLoss(String.valueOf(bulan), String.valueOf(tahun), String.valueOf(userId), String.valueOf(familyCode));

        while (cursor.moveToNext()) {
            String tipeCatatan = cursor.getString(0);
            int totalAmount = cursor.getInt(1);

            if(Objects.equals(cursor.getString(0), "Pengeluaran")){
                pieChart.addPieSlice(
                        new PieModel(
                                tipeCatatan,
                                totalAmount,
                                Color.parseColor("#fb7268")));
            } else{
                pieChart.addPieSlice(
                        new PieModel(
                                tipeCatatan,
                                totalAmount,
                                Color.parseColor("#66BB6A")));
            }


            float floatTotal = Float.parseFloat(cursor.getString(1));
            if (cursor.getString(0).equals("Pengeluaran")) {
                totalPengeluaran += floatTotal;
            } else if (cursor.getString(0).equals("Pemasukan")) {
                totalPemasukan += floatTotal;
            }
        }
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        String lossText = "Rp. " + formatter.format(totalPengeluaran);
        String profitText = "Rp. " + formatter.format(totalPemasukan);

        lossLoss.setText(lossText);
        incomeProfit.setText(profitText);


        pieChart.startAnimation();
    }

    private static String getRandomColor(){
        Random random = new Random();

        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        // Convert RGB values to hexadecimal format
        String hexColor = String.format("#%02X%02X%02X", r, g, b);

        return hexColor;
    }
}