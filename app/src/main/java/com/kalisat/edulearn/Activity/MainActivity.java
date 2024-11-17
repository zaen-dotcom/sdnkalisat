package com.kalisat.edulearn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kalisat.edulearn.Fragment.HomeFragment;
import com.kalisat.edulearn.Fragment.ProfileFragment;
import com.kalisat.edulearn.Fragment.KelasFragment;
import com.kalisat.edulearn.R;

public class MainActivity extends AppCompatActivity {

    private String userToken;

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

        // Tambahkan HomeFragment sebagai default fragment
        if (savedInstanceState == null) {
            HomeFragment homeFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_token", userToken);
            homeFragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, homeFragment);
            transaction.commit();
        }

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_home) {
                // Muat ulang HomeFragment
                selectedFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_token", userToken);
                selectedFragment.setArguments(bundle);
            } else if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            } else if (item.getItemId() == R.id.navigation_settings) {
                selectedFragment = new KelasFragment();
            }

            // Ganti fragment jika fragment dipilih
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }

    public void navigateToProfile() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile); // Set bottom navigation ke profile
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment())
                .addToBackStack(null) // Tambahkan ke backstack
                .commit();
    }
}
