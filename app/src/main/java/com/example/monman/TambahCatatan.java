package com.example.monman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TambahCatatan extends AppCompatActivity {

    private Spinner catatanSpinner, deskripsiSpinner;
    private Button btnTambah;
    private DatabaseHelper db;
    private String typeCatatan, jenisCatatan;
    private EditText amount;

    private String userId, username, familyRole, familyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_catatan);

        btnTambah = findViewById(R.id.tambahButton);
        catatanSpinner = findViewById(R.id.dropdownCatatan);
        amount = findViewById(R.id.totalAmount);
        deskripsiSpinner = findViewById(R.id.dropdownDeskripsi);
        db = new DatabaseHelper(this);
        amount.addTextChangedListener(new ThousandSeparatorTextWatcher(amount));
        getSetIntentData();

//        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();

        String [] items = new String[]{"Pengeluaran", "Pemasukan"};
        String [] jenisPengeluaran = new String[]{"Jajan", "Transportasi", "Kebutuhan", "Sewa Tempat Tinggal", "Listrik dan Air", "Pendidikan", "Kesehatan", "Rekreasi", "Pajak", "Asuransi", "Utang/Cicilan", "Belanja Barang-barang Rumah Tangga", "Hiburan"};
        String [] jenisPemasukan = new String[]{"Gaji","Pendapatan dari Usaha","Dividen","Bunga Simpanan","Pendapatan Sewa Properti","Pendapatan dari Pekerjaan Sampingan","Hadiah atau Bonus","Penjualan Aset"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, items);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        catatanSpinner.setAdapter(adapter);

        ArrayAdapter<String> pengeluaranAdapter = new ArrayAdapter<>(TambahCatatan.this, R.layout.color_spinner_layout, jenisPengeluaran);
        pengeluaranAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        deskripsiSpinner.setAdapter(pengeluaranAdapter);

        catatanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeCatatan = (String) parent.getItemAtPosition(position);

                if (typeCatatan.equals("Pengeluaran")) {
                    ArrayAdapter<String> pengeluaranAdapter = new ArrayAdapter<>(TambahCatatan.this, R.layout.color_spinner_layout, jenisPengeluaran);
                    pengeluaranAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                    deskripsiSpinner.setAdapter(pengeluaranAdapter);
                    deskripsiSpinner.setSelection(0);
                } else if (typeCatatan.equals("Pemasukan")) {
                    ArrayAdapter<String> pemasukanAdapter = new ArrayAdapter<>(TambahCatatan.this, R.layout.color_spinner_layout, jenisPemasukan);
                    pemasukanAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                    deskripsiSpinner.setAdapter(pemasukanAdapter);
                    deskripsiSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        deskripsiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jenisCatatan = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String todayDate = dateFormat.format(calendar.getTime());
                String jumlah = amount.getText().toString().replace(",", "");

                BigDecimal parsedJumlah = null;
                try {
                    parsedJumlah = new BigDecimal(jumlah);
                } catch (NumberFormatException e) {
                    // Parsing error, handle accordingly (e.g., show error message)
                    Toast.makeText(TambahCatatan.this, "Invalid amount format", Toast.LENGTH_SHORT).show();
                    return;
                }

                long tambah = db.addCatatan(userId,familyCode, typeCatatan, parsedJumlah, jenisCatatan, todayDate);

                if(tambah != -1){
//                    Toast.makeText(wallet_activity.this, typeCatatan + " " + idUser, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TambahCatatan.this, MainActivity.class);
                    intent.putExtra("userId",String.valueOf(userId));
                    intent.putExtra("username", String.valueOf(username));
                    intent.putExtra("familyRole", familyRole);
                    intent.putExtra("familyCode",familyCode);
                    // Add an extra flag to indicate direct navigation to CatatanFragment
                    intent.putExtra("directCatatanFragment", true);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    void getSetIntentData() {
        if (getIntent().hasExtra("userId")
                && getIntent().hasExtra("username")
                && getIntent().hasExtra("familyRole")
                && getIntent().hasExtra("familyCode")
        ) {

            userId = getIntent().getStringExtra("userId");
            username = getIntent().getStringExtra("username");
            familyRole = getIntent().getStringExtra("familyRole");
            familyCode = getIntent().getStringExtra("familyCode");

//            Toast.makeText(getApplicationContext(), username, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
    }
}