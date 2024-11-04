package com.kalisat.edulearn.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kalisat.edulearn.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Periksa apakah pengguna sudah login
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Jika sudah login, pindah ke MainActivity
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Tutup IntroActivity agar pengguna tidak bisa kembali ke sini
            return; // Hentikan eksekusi onCreate() agar elemen UI tidak diinisialisasi
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);

        // Menambahkan listener untuk klik di seluruh layar
        findViewById(R.id.main).setOnClickListener(v -> {
            // Intent untuk berpindah ke LoginActivity
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Menutup IntroActivity setelah pindah
        });

        // Mengatur padding untuk insets jika diperlukan
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
