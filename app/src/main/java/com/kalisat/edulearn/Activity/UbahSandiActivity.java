package com.kalisat.edulearn.Activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.kalisat.edulearn.R;

public class UbahSandiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubahsandi);

        // Inisialisasi back_icon
        ImageView backIcon = findViewById(R.id.back_icon);

        // Tambahkan listener untuk kembali ke aktivitas sebelumnya
        backIcon.setOnClickListener(v -> finish());
    }
}
