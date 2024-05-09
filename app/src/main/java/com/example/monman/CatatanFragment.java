package com.example.monman;

import android.database.Cursor;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CatatanFragment extends Fragment {

    private TextView dateView, profit, loss, totalAmount;
    private Calendar currentDate;

    private RecyclerView recyclerView;
    private String idUser, bulan, tahun;
    private ArrayList<String> id_catatan, id_user, tipe, deskripsi, total, tanggal, family_role, kode_keluarga, nama_pengguna;
    private DatabaseHelper db;

    private String  userId, username, familyRole, familyCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_catatan, container, false);

        // Retrieve data from bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("userId");
            username = bundle.getString("username");
            familyRole = bundle.getString("familyRole");
            familyCode = bundle.getString("familyCode");

            db = new DatabaseHelper(getActivity());

            ImageView leftDate = view.findViewById(R.id.monthBefore);
            ImageView rightDate = view.findViewById(R.id.monthAfter);

            recyclerView = view.findViewById(R.id.recyclerWallet);
            dateView = view.findViewById(R.id.dateText);

            profit = view.findViewById(R.id.amountProfit);
            loss = view.findViewById(R.id.amountLoss);
            totalAmount = view.findViewById(R.id.amountTotal);

            id_catatan = new ArrayList<>();
            id_user = new ArrayList<>();
            family_role = new ArrayList<>();
            kode_keluarga = new ArrayList<>();
            tipe = new ArrayList<>();
            deskripsi = new ArrayList<>();
            total = new ArrayList<>();
            tanggal = new ArrayList<>();
            nama_pengguna = new ArrayList<>();

            // Initialize the adapter
            CatatanAdapter bk = new CatatanAdapter(
                    getActivity(),
                    id_catatan,
                    id_user,
                    family_role,
                    kode_keluarga,
                    tipe,
                    deskripsi,
                    total,
                    tanggal,
                    nama_pengguna
            );

            recyclerView.setAdapter(bk);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            currentDate = Calendar.getInstance();
            updateDateView();

            leftDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDate.add(Calendar.MONTH, -1);
                    updateDateView();
                }
            });

            // Handle clicks on right arrow (next month)
            rightDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDate.add(Calendar.MONTH, 1);
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
        storeDataToArray();
    }

    void storeDataToArray() {
        try {
            int totalPengeluaran = 0, totalPemasukan = 0, totalSemua = 0;

            Cursor cursor = db.getCatatanByMonthAndYear(String.valueOf(bulan), String.valueOf(tahun), String.valueOf(userId));

            if (cursor.getCount() == 0) {
                id_catatan.clear();
                id_user.clear();
                kode_keluarga.clear();
                tipe.clear();
                deskripsi.clear();
                total.clear();
                tanggal.clear();
                family_role.clear();
                nama_pengguna.clear();

                loss.setText("0");
                profit.setText("0");
                totalAmount.setText("0");

                // Notify the adapter that the data has changed
                recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
            } else {
                // Clear the arrays before adding new data
                id_catatan.clear();
                id_user.clear();
                kode_keluarga.clear();
                tipe.clear();
                deskripsi.clear();
                total.clear();
                tanggal.clear();
                family_role.clear();
                nama_pengguna.clear();

                while (cursor.moveToNext()) {
                    id_catatan.add(cursor.getString(0));
                    id_user.add(cursor.getString(1));
                    kode_keluarga.add(cursor.getString(2));
                    tipe.add(cursor.getString(3));
                    float floatTotal = Float.parseFloat(cursor.getString(4));
                    NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
                    String formattedTotal = "Rp. "+formatter.format(floatTotal);
                    total.add(formattedTotal);
                    deskripsi.add(cursor.getString(5));
                    String[] dmy = cursor.getString(6).split("-");
                    tanggal.add(dmy[2]+"-"+dmy[1]+"-"+dmy[0]);
                    nama_pengguna.add(cursor.getString(7));
                    family_role.add(cursor.getString(8));

                    // Summing the totals based on the type
                    if (cursor.getString(3).equals("Pengeluaran")) {
                        totalPengeluaran += floatTotal;
                        totalSemua -= floatTotal;
                    } else if (cursor.getString(3).equals("Pemasukan")) {
                        totalPemasukan += floatTotal;
                        totalSemua += floatTotal;
//                        Toast.makeText((Context) this, (CharSequence) tipe, Toast.LENGTH_SHORT).show();
                    }
                }
                cursor.close();

                // Notify the adapter that the data has changed
                recyclerView.getAdapter().notifyDataSetChanged();
                NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
                String lossText = formatter.format(totalPengeluaran);
                String profitText = formatter.format(totalPemasukan);
                String totalText = formatter.format(totalSemua);
//                Toast.makeText(this, String.valueOf(totalPemasukan), Toast.LENGTH_SHORT).show();
                loss.setText(lossText);
                profit.setText(profitText);
                totalAmount.setText(totalText);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
}