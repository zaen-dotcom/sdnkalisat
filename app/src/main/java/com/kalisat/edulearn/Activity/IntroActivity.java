package com.kalisat.edulearn.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kalisat.edulearn.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Periksa token di SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", null);

        if (token != null && !token.isEmpty()) {
            // Jika token ditemukan, langsung masuk ke MainActivity
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            intent.putExtra("user_token", token);
            startActivity(intent);
            finish();
        } else {
            // Jika token tidak ditemukan, tetap di IntroActivity
            // Tambahkan listener pada layout untuk pindah ke LoginActivity saat layar disentuh
            View rootLayout = findViewById(R.id.main);
            rootLayout.setOnClickListener(v -> navigateToLogin());
        }
    }

    private void navigateToLogin() {
        // Pindah ke LoginActivity saat pengguna mengklik layar
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Tutup IntroActivity
    }
}
