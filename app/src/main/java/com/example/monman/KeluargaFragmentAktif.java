package com.example.monman;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class KeluargaFragmentAktif extends Fragment {

    private PieChart pieChart;;
    private String userId, username, familyRole, familyCode, bulan, tahun;

    private Calendar currentDate;

    private ArrayList<String> id_user, id_keluarga, nama_anggota, role_anggota;

    private RecyclerView recyclerView;
    private DatabaseHelper db;

    private TextView namakeluarga,dateView, pendapatan, pengeluaran, jumlah;
    private ImageView deleteKeluarga;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_keluarga_aktif, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("userId");
            username = bundle.getString("username");
            familyRole = bundle.getString("familyRole");
            familyCode = bundle.getString("familyCode");
            db = new DatabaseHelper(getActivity());
            recyclerView = view.findViewById(R.id.recyclerAnggotaKeluarga);
            namakeluarga = view.findViewById(R.id.namaKeluargaText);
            deleteKeluarga = view.findViewById(R.id.deleteFamilyBtn);
            pieChart = view.findViewById(R.id.piechart);
            ImageView addMonthBtn = view.findViewById(R.id.addMonth);
            ImageView decMonthBtn = view.findViewById(R.id.decMonth);
            currentDate = Calendar.getInstance();
            dateView = view.findViewById(R.id.monthYearGraph);
            pendapatan = view.findViewById(R.id.income);
            pengeluaran = view.findViewById(R.id.loss);
            jumlah = view.findViewById(R.id.total);

            id_user = new ArrayList<>();
            id_keluarga = new ArrayList<>();
            nama_anggota = new ArrayList<>();
            role_anggota = new ArrayList<>();

            AnggotaKeluargaAdapter bk = new AnggotaKeluargaAdapter(
                    getActivity(),
                    id_user,
                    id_keluarga,
                    nama_anggota,
                    role_anggota,
                    familyRole,
                    userId,
                    username);
            recyclerView.setAdapter(bk);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            setData();
            updateDateView();
            deleteKeluarga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!Objects.equals(familyRole, "Ayah")){
                        Toast.makeText(getActivity(), "Anda Tidak Memiliki Akses", Toast.LENGTH_SHORT).show();
                    } else{
                        DeleteFamilyDialog dialog = new DeleteFamilyDialog(getActivity(), userId, username, familyRole, familyCode);
                        dialog.show();
                    }
                }
            });

            addMonthBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDate.add(Calendar.MONTH, 1);
                    updateDateView();
                }
            });

            decMonthBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDate.add(Calendar.MONTH, -1);
                    updateDateView();
                }
            });

        }
        return view;
    }

    private void updateDateView() {
        SimpleDateFormat dateFormatText = new SimpleDateFormat("MMMM yyyy", new Locale("id", "ID"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM yyyy", new Locale("id", "ID"));
        String formattedDate = dateFormat.format(currentDate.getTime());
        String formattedDateText = dateFormatText.format(currentDate.getTime());
        // Split the formatted date string to extract month and year
        String[] parts = formattedDate.split(" ");
        if (parts.length == 2) {
            bulan = parts[0]; // Extracted month
            tahun = parts[1]; // Extracted year
        }
        dateView.setText(formattedDateText);
        setDataGraph();
    }

    void setData() {
        try {
            Cursor cursor = db.getUserFamily(familyCode);
            Cursor family = db.getFamily(familyCode);

            if(family.getCount()>0){
                family.moveToFirst();
                String familyName = family.getString(0)+ " - " + family.getString(1) + "'s"  ;
                namakeluarga.setText(familyName);
            }
            if (cursor.getCount() == 0) {
                id_user.clear();
                id_keluarga.clear();
                nama_anggota.clear();
                role_anggota.clear();

                // Notify the adapter that the data has changed
                recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
            } else {
                // Clear the arrays before adding new data
                id_user.clear();
                id_keluarga.clear();
                nama_anggota.clear();
                role_anggota.clear();

                while (cursor.moveToNext()) {
                    id_user.add(cursor.getString(0));
                    nama_anggota.add(cursor.getString(1));
                    role_anggota.add(cursor.getString(5));
                    id_keluarga.add(cursor.getString(4));
                }
                cursor.close();

                // Notify the adapter that the data has changed
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    void setDataGraph() {
        pieChart.clearChart();
        int totalPengeluaran = 0, totalPemasukan = 0, totalSemua = 0;
        try {
            Cursor cursor = db.getDataforGraphFamily(String.valueOf(bulan), String.valueOf(tahun), String.valueOf(userId), String.valueOf(familyCode));
            if(cursor.getCount() == 0){
                pengeluaran.setText("Rp. 0");
                pendapatan.setText("Rp. 0");
                jumlah.setText("Ro. 0");
                recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
            } else{
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
                        totalSemua -= floatTotal;
                    } else if (cursor.getString(0).equals("Pemasukan")) {
                        totalPemasukan += floatTotal;
                        totalSemua += floatTotal;
                    }
                }

                NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
                String lossText = "Rp. " + formatter.format(totalPengeluaran);
                String profitText = "Rp. " + formatter.format(totalPemasukan);
                String totalText = "Rp. " + formatter.format(totalSemua);
                pengeluaran.setText(lossText);
                pendapatan.setText(profitText);
                jumlah.setText(totalText);
                pieChart.startAnimation();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
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
