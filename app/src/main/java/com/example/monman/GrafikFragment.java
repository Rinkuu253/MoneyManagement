package com.example.monman;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import com.example.monman.ColorGenerator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


public class GrafikFragment extends Fragment {

    PieChart pieChart;
    private DatabaseHelper db;
    private RecyclerView recyclerView;
    private ArrayList<String> deskripsiChart, totalChart;
    private String userId, username, familyRole, familyCode, bulan, tahun, awalMinggu, akhirMinggu;

    private ImageView addMonthBtn, decMonthBtn, addWeekBtn, decWeekBtn;
    private TextView dateView, weekView;
    private Calendar currentDate, currentWeek;

    private LineChart lineChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grafik, container, false);
// Retrieve data from bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
             userId = bundle.getString("userId");
             username = bundle.getString("username");
             familyRole = bundle.getString("familyRole");
             familyCode = bundle.getString("familyCode");
            recyclerView = view.findViewById(R.id.recycleCatatanSum);
            addMonthBtn = view.findViewById(R.id.addMonth);
            decMonthBtn = view.findViewById(R.id.decMonth);
            pieChart = view.findViewById(R.id.piechart);
            dateView = view.findViewById(R.id.monthYearGraph);
            addWeekBtn = view.findViewById(R.id.addWeek);
            decWeekBtn = view.findViewById(R.id.decWeek);
            weekView = view.findViewById(R.id.weekOfMonth);
            lineChart = view.findViewById(R.id.lineChart);

            db = new DatabaseHelper(getActivity());
            currentDate = Calendar.getInstance();
            currentWeek = Calendar.getInstance();

            deskripsiChart = new ArrayList<>();
            totalChart = new ArrayList<>();
            ChartCatatanAdapterDescription bk = new ChartCatatanAdapterDescription(
                    getActivity(),
                    deskripsiChart,
                    totalChart
            );

            recyclerView.setAdapter(bk);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            updateDateView();
            updateWeekView();

//            makeGraph();
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

            addWeekBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentWeek.add(Calendar.WEEK_OF_MONTH, 1);
                    updateWeekView();
                }
            });

            decWeekBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentWeek.add(Calendar.WEEK_OF_MONTH, -1);
                    updateWeekView();
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
        setData();
    }

    private void updateWeekView() {
        SimpleDateFormat dateFormatStartWeek = new SimpleDateFormat("d MMMM - ", new Locale("id", "ID"));
        SimpleDateFormat dateFormatEndWeek = new SimpleDateFormat("d MMMM yyyy", new Locale("id", "ID"));
        SimpleDateFormat dateFormatWeekText = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID"));

        Calendar startOfWeek = (Calendar) currentWeek.clone();
        startOfWeek.set(Calendar.DAY_OF_WEEK, startOfWeek.getFirstDayOfWeek());

        Calendar endOfWeek = (Calendar) startOfWeek.clone();
        endOfWeek.add(Calendar.DAY_OF_YEAR, 6);

        String formattedStartDate = dateFormatStartWeek.format(startOfWeek.getTime());
        String formattedEndDate = dateFormatEndWeek.format(endOfWeek.getTime());

        awalMinggu = dateFormatWeekText.format(startOfWeek.getTime());
        akhirMinggu = dateFormatWeekText.format(endOfWeek.getTime());

        String formattedDateText = formattedStartDate + formattedEndDate;

        weekView.setText(formattedDateText);
        makeGraph();
    }


    private void setData(){
        pieChart.clearChart();

        Cursor cursor = db.getDataforGraph(String.valueOf(bulan), String.valueOf(tahun), String.valueOf(userId), String.valueOf(familyCode));
        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), familyCode, Toast.LENGTH_SHORT).show();

            deskripsiChart.clear();
            totalChart.clear();
            recyclerView.getAdapter().notifyDataSetChanged();
            Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
        } else{
            deskripsiChart.clear();
            totalChart.clear();
            while (cursor.moveToNext()) {

                pieChart.addPieSlice(
                        new PieModel(
                                cursor.getString(0),
                                cursor.getInt(1),
                                Color.parseColor(getRandomColor())));

                deskripsiChart.add(cursor.getString(0));
                float floatTotal = Float.parseFloat(cursor.getString(1));
                NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
                String formattedTotal = "Rp. "+formatter.format(floatTotal);
                totalChart.add(formattedTotal);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            pieChart.startAnimation();
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

    private void makeGraph(){
        ArrayList<Entry> entries = new ArrayList<>();

        Cursor cursor = db.getDataProfitLossPerWeek(awalMinggu, akhirMinggu, userId, familyCode);
            for (int i = 0; i < 7; i++) {
                SimpleDateFormat dateFormatWeekText = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID"));
                SimpleDateFormat dateFormatText = new SimpleDateFormat("d", new Locale("id", "ID"));
                Calendar calendar = (Calendar) currentWeek.clone();
                calendar.add(Calendar.DAY_OF_YEAR, i);
//                int dayOfYear = calendar.get(Calendar.DAY_OF_MONTH); // Get day of the year
                String currentDate = dateFormatWeekText.format(calendar.getTime());
                boolean entryFound = false;
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String cursorDate = cursor.getString(0);
                    if (cursorDate.equals(currentDate)) {
                        if(Objects.equals(cursor.getString(1), "Pengeluaran")){
                            entries.add(new Entry(i, cursor.getInt(2)));
                            entryFound = true;
                            break;
                        }
                    }
                    cursor.moveToNext();
                }

                if (!entryFound) {
                    entries.add(new Entry(i, 0));
                }
            }
            cursor.close();



        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.RED);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        Description desc = new Description();
        desc.setText("Periode Mingguan");
        lineChart.setDescription(desc);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false); // Disable legend

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false); // Disable right Y-axis

        lineChart.invalidate();
    }
}