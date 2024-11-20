package com.kalisat.edulearn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kalisat.edulearn.Fragment.HomeFragment;
import com.kalisat.edulearn.Fragment.ProfileFragment;
import com.kalisat.edulearn.Fragment.KelasFragment;
import com.kalisat.edulearn.R;

public class MainActivity extends AppCompatActivity {

    private String userToken;
    private boolean doubleBackToExitPressedOnce = false; // Variabel untuk deteksi klik back dua kali

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ambil token dari Intent
        userToken = getIntent().getStringExtra("user_token");

        // Jika token kosong, arahkan ke LoginActivity
        if (userToken == null || userToken.isEmpty()) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Tambahkan HomeFragment sebagai fragment default
        if (savedInstanceState == null) {
            navigateToFragment(new HomeFragment(), false);
        }

        setupBottomNavigation();

        // Setup Back Press Handling
        setupOnBackPressedDispatcher();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            boolean addToBackStack = false;

            if (item.getItemId() == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
                addToBackStack = true; // Tambahkan ke backstack untuk fragment selain Home
            } else if (item.getItemId() == R.id.navigation_settings) {
                selectedFragment = new KelasFragment();
                addToBackStack = true; // Tambahkan ke backstack untuk fragment selain Home
            }

            if (selectedFragment != null) {
                navigateToFragment(selectedFragment, addToBackStack);
            }

            return true;
        });
    }

    public void navigateToProfile() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile); // Set tab ke Profile
        navigateToFragment(new ProfileFragment(), true);
    }

    private void navigateToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null); // Tambahkan ke backstack
        }

        transaction.commit();
    }

    private void setupOnBackPressedDispatcher() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                // Tangani fragment utama
                if (currentFragment instanceof HomeFragment
                        || currentFragment instanceof ProfileFragment
                        || currentFragment instanceof KelasFragment) {

                    if (doubleBackToExitPressedOnce) {
                        finish(); // Tutup aplikasi
                        return;
                    }

                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(MainActivity.this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

                    // Reset status setelah 2 detik
                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1500);
                } else {
                    // Gunakan default behavior untuk fragment lain
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    } else {
                        finish();
                    }
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
