package com.kalisat.edulearn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kalisat.edulearn.Fragment.LatsolFragment;
import com.kalisat.edulearn.Fragment.TugasFragment;
import com.kalisat.edulearn.R;

public class DetailMapelActivity extends AppCompatActivity {

    private int idMapel;
    private String namaMapel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mapel);

        // Ambil data dari Intent
        Intent intent = getIntent();
        idMapel = intent.getIntExtra("id_mapel", -1);  // Ambil ID mata pelajaran
        namaMapel = intent.getStringExtra("nama_mapel");  // Ambil Nama mata pelajaran

        // Cek apakah ID valid
        if (idMapel != -1 && namaMapel != null) {
            // Set default fragment (TugasFragment)
            loadFragment(TugasFragment.newInstance(idMapel, namaMapel));
        } else {
            Toast.makeText(this, "ID atau Nama Mata Pelajaran Tidak Valid", Toast.LENGTH_SHORT).show();
        }

        // Setup BottomNavigationView
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Pilih fragment berdasarkan tab yang dipilih
            if (item.getItemId() == R.id.navigation_tugas) {
                selectedFragment = TugasFragment.newInstance(idMapel, namaMapel);
            } else if (item.getItemId() == R.id.navigation_latihan) {
                selectedFragment = LatsolFragment.newInstance(idMapel);
            }

            return loadFragment(selectedFragment);
        });
    }

    // Fungsi untuk memuat fragment
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
