package com.kalisat.edulearn.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Adapter.JadwalAdapterGrouped;
import com.kalisat.edulearn.Model.ModelJadwalGrouped;
import com.kalisat.edulearn.R;
import com.kalisat.edulearn.ViewModel.JadwalViewModel;

import java.util.ArrayList;

public class DetailJadwalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JadwalAdapterGrouped jadwalAdapterGrouped;
    private JadwalViewModel jadwalViewModel;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jadwal);

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi adapter dengan list kosong
        jadwalAdapterGrouped = new JadwalAdapterGrouped(this, new ArrayList<>());
        recyclerView.setAdapter(jadwalAdapterGrouped);

        // Inisialisasi ViewModel
        jadwalViewModel = new ViewModelProvider(this).get(JadwalViewModel.class);

        // Observasi data jadwal dari ViewModel
        jadwalViewModel.getJadwalGroupedList().observe(this, groupedList -> {
            jadwalAdapterGrouped.updateData(groupedList);
        });

        // Observasi error
        jadwalViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Log.e("Error", errorMessage);
            }
        });

        // Panggil API melalui ViewModel
        requestQueue = Volley.newRequestQueue(this);
        String token = getSharedPreferences("user_session", MODE_PRIVATE).getString("user_token", "");
        if (!token.isEmpty()) {
            jadwalViewModel.fetchJadwal(requestQueue, token);
        }

        // Tangani tombol kembali
        findViewById(R.id.ic_kembali).setOnClickListener(v -> {
            finish(); // Kembali ke halaman sebelumnya
        });
    }
}
