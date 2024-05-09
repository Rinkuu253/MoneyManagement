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
import java.util.Arrays;
import java.util.Objects;

public class EditCatatan extends AppCompatActivity {

    private Spinner catatanSpinner, deskripsiSpinner;
    private Button btnEdit, btnDelete;
    private DatabaseHelper db;
    private String typeCatatan, jenisCatatan;
    private EditText amount;

    private String catatanId, userId, tipe, deskripsi, total, username, familyRole, familyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_catatan);

        btnEdit = findViewById(R.id.editButton);
        btnDelete = findViewById(R.id.deleteButton);
        catatanSpinner = findViewById(R.id.dropdownCatatan);
        amount = findViewById(R.id.totalAmount);
        deskripsiSpinner = findViewById(R.id.dropdownDeskripsi);
        db = new DatabaseHelper(this);
        amount.addTextChangedListener(new ThousandSeparatorTextWatcher(amount));

//        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();

        String [] items = new String[]{"Pengeluaran", "Pemasukan"};
        String [] jenisPengeluaran = new String[]{"Jajan", "Transportasi", "Kebutuhan", "Sewa Tempat Tinggal", "Listrik dan Air", "Pendidikan", "Kesehatan", "Rekreasi", "Pajak", "Asuransi", "Utang/Cicilan", "Belanja Barang-barang Rumah Tangga", "Hiburan"};
        String [] jenisPemasukan = new String[]{"Gaji","Pendapatan dari Usaha","Dividen","Bunga Simpanan","Pendapatan Sewa Properti","Pendapatan dari Pekerjaan Sampingan","Hadiah atau Bonus","Penjualan Aset"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, items);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        catatanSpinner.setAdapter(adapter);

        getSetIntentData();

        catatanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeCatatan = (String) parent.getItemAtPosition(position);

                if (typeCatatan.equals("Pengeluaran")) {
                    int indexPengeluaran = Arrays.asList(jenisPengeluaran).indexOf(deskripsi);
                    ArrayAdapter<String> pengeluaranAdapter = new ArrayAdapter<>(EditCatatan.this, R.layout.color_spinner_layout, jenisPengeluaran);
                    pengeluaranAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                    deskripsiSpinner.setAdapter(pengeluaranAdapter);
                    deskripsiSpinner.setSelection(indexPengeluaran);
                } else if (typeCatatan.equals("Pemasukan")) {
                    int indexPemasukan = Arrays.asList(jenisPemasukan).indexOf(deskripsi);
                    ArrayAdapter<String> pemasukanAdapter = new ArrayAdapter<>(EditCatatan.this, R.layout.color_spinner_layout, jenisPemasukan);
                    pemasukanAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
                    deskripsiSpinner.setAdapter(pemasukanAdapter);
                    deskripsiSpinner.setSelection(indexPemasukan);
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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jumlah = amount.getText().toString().replace(",", "");

                BigDecimal parsedJumlah = null;
                try {
                    parsedJumlah = new BigDecimal(jumlah);
                } catch (NumberFormatException e) {
                    // Parsing error, handle accordingly (e.g., show error message)
                    Toast.makeText(EditCatatan.this, "Invalid amount format", Toast.LENGTH_SHORT).show();
                    return;
                }

                long tambah = db.updateCatatan(catatanId,typeCatatan, parsedJumlah, jenisCatatan);

                if(tambah != -1){
//                    Toast.makeText(wallet_activity.this, typeCatatan + " " + idUser, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditCatatan.this, MainActivity.class);
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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (catatanId != null && !catatanId.isEmpty()) {
                    try {
                        boolean recordDeleteBw = db.deleteCatatan(catatanId);
                        if (recordDeleteBw) {
                            Toast.makeText(getApplicationContext(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditCatatan.this, MainActivity.class);
                            intent.putExtra("userId",String.valueOf(userId));
                            intent.putExtra("username", String.valueOf(username));
                            intent.putExtra("familyRole", familyRole);
                            intent.putExtra("familyCode",familyCode);
                            // Add an extra flag to indicate direct navigation to CatatanFragment
                            intent.putExtra("directCatatanFragment", true);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Data Gagal Dihapus", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Invalid Catatan ID", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Catatan ID is Null or Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getSetIntentData() {
        if (getIntent().hasExtra("id_catatan")
                && getIntent().hasExtra("tipe")
                && getIntent().hasExtra("deskripsi")
                && getIntent().hasExtra("total")) {

            catatanId = getIntent().getStringExtra("id_catatan");
            userId = getIntent().getStringExtra("id_user");
            familyCode = getIntent().getStringExtra("kode_keluarga");
            username = getIntent().getStringExtra("nama");
            familyRole =getIntent().getStringExtra("role");
            tipe = getIntent().getStringExtra("tipe");
            deskripsi = getIntent().getStringExtra("deskripsi");
            String jumlah = getIntent().getStringExtra("total");
            total = jumlah.replace("Rp. ", "");

            amount.setText(total);

            if(Objects.equals(tipe, "Pengeluaran")){
                catatanSpinner.setSelection(0);
            } else{
                catatanSpinner.setSelection(1);
            }

        } else {
            Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
        }
    }
}