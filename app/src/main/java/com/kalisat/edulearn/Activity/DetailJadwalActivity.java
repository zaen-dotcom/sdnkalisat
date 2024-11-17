package com.kalisat.edulearn.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.kalisat.edulearn.R;

public class DetailJadwalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jadwal);

        // Ambil referensi ke ImageView
        ImageView icKembali = findViewById(R.id.ic_kembali);

        // Tambahkan listener untuk kembali ke aktivitas sebelumnya
        icKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Kembali ke aktivitas sebelumnya
            }
        });
    }
}
