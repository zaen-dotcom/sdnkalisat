package com.kalisat.edulearn;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    }

    @Override
    public void onBackPressed() {
        if (!isInHomeView) {
            // Jika bukan di home, kembalikan ke home
            homeView.setVisibility(View.VISIBLE);
            isInHomeView = true;
            // Pop semua fragment untuk kembali ke home
            getSupportFragmentManager().popBackStackImmediate(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
        } else {
            // Jika sudah di home, keluar dari aplikasi
            super.onBackPressed();
        }
    }
}
