package com.kalisat.edulearn.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kalisat.edulearn.Fragment.NotifFragment;
import com.kalisat.edulearn.Fragment.ProfileFragment;
import com.kalisat.edulearn.Fragment.SettingFragment;
import com.kalisat.edulearn.Network.ApiClient;
import com.kalisat.edulearn.Network.ApiService;
import com.kalisat.edulearn.Model.UserIdRequest;
import com.kalisat.edulearn.Model.UserResponse;
import com.kalisat.edulearn.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private View homeView; // Reference untuk layout home (ScrollView)
    private boolean isInHomeView = true; // Flag untuk mengetahui apakah sedang di home

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Menggunakan layout activity_main.xml

        homeView = findViewById(R.id.scrollView2); // Mengambil referensi ke ScrollView home

        // Ambil user_id dan role dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1); // Ambil user_id, -1 jika tidak ditemukan
        String nama = sharedPreferences.getString("nama", "Pengguna"); // Ambil nama, default "Pengguna" jika tidak ditemukan
        String role = sharedPreferences.getString("role", ""); // Ambil role pengguna

        // Tambahkan kode untuk menampilkan nama di TextView
        TextView welcomeTextView = findViewById(R.id.welcome);
        String formattedName = capitalizeEachWord(nama); // Format nama
        welcomeTextView.setText("Halo " + formattedName + "!");

        // Cek apakah userId valid, jika valid panggil loadUserName
        if (userId != -1 && !role.isEmpty()) {
            loadUserName(userId, role, welcomeTextView); // Panggil fungsi untuk memuat nama pengguna dari server jika diperlukan
        }

        // Listener untuk navigasi bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
                isInHomeView = false;
                homeView.setVisibility(View.GONE);
            } else if (item.getItemId() == R.id.navigation_settings) {
                selectedFragment = new SettingFragment();
                isInHomeView = false;
                homeView.setVisibility(View.GONE);
            } else if (item.getItemId() == R.id.navigation_home) {
                isInHomeView = true;
                homeView.setVisibility(View.VISIBLE);
                getSupportFragmentManager().popBackStackImmediate(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                selectedFragment = null;
            } else if (item.getItemId() == R.id.navigation_notif) {
                selectedFragment = new NotifFragment();
                isInHomeView = false;
                homeView.setVisibility(View.GONE);
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .addToBackStack(null)
                        .commit();
            }

            return true;
        });

        // Default tampilan saat aplikasi dibuka
        if (savedInstanceState == null) {
            homeView.setVisibility(View.VISIBLE);
        }

        // Kode yang sebelumnya Anda sebutkan, tetap dipertahankan tanpa perubahan
        View tugasLayout = findViewById(R.id.tugas_layout);
        tugasLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TugasActivity.class);
            startActivity(intent);
        });

        View nilaiLayout = findViewById(R.id.nilai_layout);
        nilaiLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NilaiActivity.class);
            startActivity(intent);
        });

        View latsolLayout = findViewById(R.id.latsol_layout);
        latsolLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LatSolActivity.class);
            startActivity(intent);
        });

        View kelasLayout = findViewById(R.id.kelas_layout);
        kelasLayout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, KelasActivity.class);
            startActivity(intent);
        });

        ImageView fotoProfil = findViewById(R.id.fotoprofil);
        fotoProfil.setOnClickListener(v -> {
            Fragment profileFragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, profileFragment)
                    .addToBackStack(null)
                    .commit();
            homeView.setVisibility(View.GONE);
            isInHomeView = false;
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
            homeView.setVisibility(View.VISIBLE);
            isInHomeView = true;
            getSupportFragmentManager().popBackStackImmediate(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else {
            super.onBackPressed();
        }
    }

    // Fungsi untuk memuat nama pengguna menggunakan Retrofit
    private void loadUserName(int userId, String role, TextView welcomeTextView) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        UserIdRequest request = new UserIdRequest(userId, role); // Pastikan UserIdRequest mendukung parameter role

        apiService.getUserName(request).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    String nama = response.body().getUser().getNama();
                    String formattedName = capitalizeEachWord(nama);
                    welcomeTextView.setText("Halo, " + formattedName + "!");
                } else {
                    welcomeTextView.setText("Gagal memuat data pengguna");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                welcomeTextView.setText("Tidak dapat terhubung ke server");
            }
        });
    }

    private String capitalizeEachWord(String str) {
        String[] words = str.split(" ");
        StringBuilder capitalizedWords = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                capitalizedWords.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return capitalizedWords.toString().trim();
    }
}
