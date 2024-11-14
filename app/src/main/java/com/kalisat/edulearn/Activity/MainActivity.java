package com.kalisat.edulearn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kalisat.edulearn.Fragment.ProfileFragment;
import com.kalisat.edulearn.Fragment.KelasFragment;
import com.kalisat.edulearn.R;

public class MainActivity extends AppCompatActivity {

    private String userToken; // Token user untuk autentikasi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ambil token dari Intent
        userToken = getIntent().getStringExtra("user_token");

        if (userToken == null || userToken.isEmpty()) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        setupBottomNavigation();
        setupListeners();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            } else if (item.getItemId() == R.id.navigation_settings) {
                selectedFragment = new KelasFragment();
            } else if (item.getItemId() == R.id.navigation_home) {
                // Hapus semua fragment dari backstack saat kembali ke Home
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                // Tidak perlu melanjutkan fragment swap jika di Home
                return true;
            }

            if (selectedFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, selectedFragment);
                transaction.addToBackStack(null); // Tambahkan ke backstack untuk navigasi balik
                transaction.commit();
            }

            return true;
        });
    }

    private void setupListeners() {
        // Listener untuk foto profil
        ImageView fotoProfil = findViewById(R.id.profile_image);
        fotoProfil.setOnClickListener(v -> {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate(); // Kembali ke fragment sebelumnya di stack
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

            // Atur BottomNavigationView agar sesuai dengan fragment aktif
            if (currentFragment instanceof ProfileFragment) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
            } else if (currentFragment instanceof KelasFragment) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
            } else {
                bottomNavigationView.setSelectedItemId(R.id.navigation_home); // Set ke Home jika sudah tidak ada backstack
            }
        } else {
            // Jika sudah di root (Home), keluar dari aplikasi
            super.onBackPressed();
        }
    }
}
