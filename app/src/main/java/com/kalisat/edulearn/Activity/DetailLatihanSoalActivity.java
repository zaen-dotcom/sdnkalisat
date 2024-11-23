package com.kalisat.edulearn.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Model.ModelDetailLatihanSoal;
import com.kalisat.edulearn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailLatihanSoalActivity extends AppCompatActivity {

    private TextView tvJudul, tvJumlah, tvWaktuMulai, tvWaktuBerakhir;
    private Button btnMulai, btnKembali;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;

    private int idSoal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_latihan_soal);

        // Inisialisasi elemen UI
        tvJudul = findViewById(R.id.tv_judul);
        tvJumlah = findViewById(R.id.tv_jumlah);
        tvWaktuMulai = findViewById(R.id.tv_waktumulai);
        tvWaktuBerakhir = findViewById(R.id.tv_waktuberakhir);
        btnMulai = findViewById(R.id.btn_mulai);
        btnKembali = findViewById(R.id.btn_kembali);
        progressBar = findViewById(R.id.progressBar);

        // Inisialisasi RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Ambil ID soal dari Intent
        idSoal = getIntent().getIntExtra("id_soal", -1);

        // Periksa apakah soal sudah dikerjakan
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        boolean soalDikerjakan = sharedPreferences.getBoolean("soal_dikerjakan_" + idSoal, false);

        if (soalDikerjakan) {
            btnMulai.setVisibility(View.GONE); // Sembunyikan tombol Mulai jika soal sudah dikerjakan
        }

        if (idSoal != -1) {
            // Tampilkan progress bar
            progressBar.setVisibility(View.VISIBLE);
            // Panggil API untuk mengambil detail soal
            getDetailLatihanSoal(idSoal);
        } else {
            Toast.makeText(this, "ID soal tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }

        // Listener untuk tombol Kembali
        btnKembali.setOnClickListener(v -> finish());

        // Listener untuk tombol Mulai
        btnMulai.setOnClickListener(v -> {
            Intent intent = new Intent(DetailLatihanSoalActivity.this, ProsesLatihanSoalActivity.class);
            intent.putExtra("id_latihan_soal", idSoal);
            startActivityForResult(intent, 1); // Mulai aktivitas dengan result
        });
    }


    private void getDetailLatihanSoal(int idSoal) {
        String url = "http://192.168.159.228:8000/api/latihan-soal/" + idSoal;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");

                            if (dataArray.length() > 0) {
                                JSONObject dataObj = dataArray.getJSONObject(0);

                                String judulSoal = dataObj.getString("judul_soal");
                                int jumlahSoal = dataObj.getInt("jumlah_soal");
                                String tanggalSoal = dataObj.getString("tanggal_soal");
                                String deadline = dataObj.getString("deadline");
                                int id = dataObj.getInt("id");

                                ModelDetailLatihanSoal model = new ModelDetailLatihanSoal(judulSoal, jumlahSoal, tanggalSoal, deadline, id);

                                updateUI(model);
                            } else {
                                Toast.makeText(this, "Data soal tidak ditemukan.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Gagal memuat detail soal.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Kesalahan parsing data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal memuat data.", Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("user_token", "");

                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void updateUI(ModelDetailLatihanSoal model) {
        tvJudul.setText(model.getJudulSoal());
        tvJumlah.setText(String.valueOf(model.getJumlahSoal()));
        tvWaktuMulai.setText(model.getTanggalSoal());
        tvWaktuBerakhir.setText(model.getDeadline());
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null && "completed".equals(data.getStringExtra("result"))) {
                Toast.makeText(this, "Latihan soal selesai!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
