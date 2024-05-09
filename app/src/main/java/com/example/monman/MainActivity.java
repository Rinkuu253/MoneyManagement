package com.example.monman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.view.MenuItem;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.monman.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private FloatingActionButton tambahCatBtn;

    private String userId, username, familyRole, familyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        tambahCatBtn = binding.tambahCatatanButton;
        setContentView(binding.getRoot());
        getSetIntentData();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId); // Put data into the bundle
        bundle.putString("username", username);
        bundle.putString("familyRole", familyRole);
        bundle.putString("familyCode", familyCode);

//        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();

        replaceFragment(new HomeFragment(), bundle);
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home){
                replaceFragment(new HomeFragment(), bundle);
            } else if(item.getItemId() == R.id.catatan){
                replaceFragment(new CatatanFragment(), bundle);
            }else if(item.getItemId() == R.id.keluarga){
                if(Objects.equals(familyRole, "Pribadi")){
//                    Toast.makeText(this, familyRole, Toast.LENGTH_SHORT).show();
                    replaceFragment(new KeluargaFragment(), bundle);
                } else{
                    replaceFragment(new KeluargaFragmentAktif(), bundle);
                }
            }else if(item.getItemId() == R.id.grafik){
                replaceFragment(new GrafikFragment(), bundle);
            }
            return true;
        });

        tambahCatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TambahCatatan.class);
                intent.putExtra("userId",String.valueOf(userId));
                intent.putExtra("username", String.valueOf(username));
                intent.putExtra("familyRole", familyRole);
                intent.putExtra("familyCode",familyCode);
                startActivity(intent);
//                finish();
            }
        });

        if (getIntent().getBooleanExtra("directCatatanFragment", false)) {
            // If present, navigate to CatatanFragment directly
            replaceFragment(new CatatanFragment(), bundle);
            binding.bottomNavigationView.setSelectedItemId(R.id.catatan);
        }

        if (getIntent().getBooleanExtra("directFamilyFragment", false)) {
            // If present, navigate to CatatanFragment directly
            if(Objects.equals(familyRole, "Pribadi")){
//                    Toast.makeText(this, familyRole, Toast.LENGTH_SHORT).show();
                replaceFragment(new KeluargaFragment(), bundle);
                binding.bottomNavigationView.setSelectedItemId(R.id.keluarga);
            } else{
                replaceFragment(new KeluargaFragmentAktif(), bundle);
                binding.bottomNavigationView.setSelectedItemId(R.id.keluarga);
            }
        }


    }

    private void replaceFragment(Fragment fragment, Bundle bundle){
        fragment.setArguments(bundle); // Pass the bundle to the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
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