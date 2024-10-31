package com.kalisat.edulearn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kalisat.edulearn.Fragment.NotifFragment;
import com.kalisat.edulearn.Fragment.ProfileFragment;
import com.kalisat.edulearn.R;
import com.kalisat.edulearn.Fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

    private View homeView; // Reference untuk layout home (ScrollView)
    private boolean isInHomeView = true; // Flag untuk mengetahui apakah sedang di home

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Menggunakan layout activity_main.xml

        homeView = findViewById(R.id.scrollView2); // Mengambil referensi ke ScrollView home

        // Listener untuk navigasi bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
                isInHomeView = false; // Sekarang bukan di home
                homeView.setVisibility(View.GONE); // Sembunyikan home view
            } else if (item.getItemId() == R.id.navigation_settings) {
                selectedFragment = new SettingFragment();
                isInHomeView = false; // Sekarang bukan di home
                homeView.setVisibility(View.GONE); // Sembunyikan home view
            } else if (item.getItemId() == R.id.navigation_home) {
                isInHomeView = true; // Kembali ke home
                homeView.setVisibility(View.VISIBLE); // Tampilkan home view
                // Keluarkan fragment saat di home
                getSupportFragmentManager().popBackStackImmediate(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                selectedFragment = null;
            } else if (item.getItemId() == R.id.navigation_notif) {
                selectedFragment = new NotifFragment(); // Mengatur NotifFragment sebagai fragment yang ditampilkan
                isInHomeView = false; // Sekarang bukan di home
                homeView.setVisibility(View.GONE); // Sembunyikan home view
            }


            // Mengganti fragment hanya jika fragment lain yang dipilih
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .addToBackStack(null)  // Tambahkan ke back stack
                        .commit();
            }

            return true;
        });

        // Mengatur default tampilan saat aplikasi dibuka
        if (savedInstanceState == null) {
            homeView.setVisibility(View.VISIBLE); // Pastikan homeView terlihat saat pertama kali dibuka
        }

        View tugasLayout = findViewById(R.id.tugas_layout);
        tugasLayout.setOnClickListener(v -> {
            // Aksi saat Codiger diklik
            Intent intent = new Intent(MainActivity.this, TugasActivity.class);
            startActivity(intent);
        });

        // Bengkel (Nilai)
        View nilaiLayout = findViewById(R.id.nilai_layout);
        nilaiLayout.setOnClickListener(v -> {
            // Aksi saat Bengkel diklik
            Intent intent = new Intent(MainActivity.this, NilaiActivity.class);
            startActivity(intent);
        });

        // Matematika (Latihan Soal)
        View latsolLayout = findViewById(R.id.latsol_layout);
        latsolLayout.setOnClickListener(v -> {
            // Aksi saat Matematika diklik
            Intent intent = new Intent(MainActivity.this, LatSolActivity.class);
            startActivity(intent);
        });

        // Manajemen (Kelas)
        View kelasLayout = findViewById(R.id.kelas_layout);
        kelasLayout.setOnClickListener(v -> {
            // Aksi saat Manajemen diklik
            Intent intent = new Intent(MainActivity.this, KelasActivity.class);
            startActivity(intent);
        });

        // Tambahkan listener untuk foto profil
        ImageView fotoProfil = findViewById(R.id.fotoprofil);
        fotoProfil.setOnClickListener(v -> {
            // Berpindah ke ProfileFragment saat foto profil diklik
            Fragment profileFragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, profileFragment)
                    .addToBackStack(null)  // Tambahkan ke back stack
                    .commit();
            homeView.setVisibility(View.GONE); // Sembunyikan home view saat pindah ke ProfileFragment
            isInHomeView = false; // Flag bahwa kita tidak lagi di home

            // Perbarui status BottomNavigationView ke item profil (gunakan variabel existing)
            bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        });


        View lihatSemua = findViewById(R.id.lihat_semua);
        lihatSemua.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, KursusActivity.class);
            startActivity(intent);
        });


        View listKursus = findViewById(R.id.listkursus);
        listKursus.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, KursusActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        if (!isInHomeView) {
            // Jika bukan di home, kembalikan ke home
            homeView.setVisibility(View.VISIBLE);
            isInHomeView = true;
            // Pop semua fragment untuk kembali ke home
            getSupportFragmentManager().popBackStackImmediate(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);

            // Set BottomNavigationView ke posisi Home
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else {
            // Jika sudah di home, keluar dari aplikasi
            super.onBackPressed();
        }
    }
}