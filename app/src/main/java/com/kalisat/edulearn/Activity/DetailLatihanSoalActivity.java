package com.kalisat.edulearn.Activity;

import android.app.AlertDialog;
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

    private TextView tvJudul, tvJumlah, tvWaktuMulai, tvWaktuBerakhir, tvNilai; // Tambahkan tvNilai
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
        tvNilai = findViewById(R.id.tv_nilai); // Inisialisasi tvNilai
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

            // Panggil API untuk mengambil nilai soal
            getNilaiLatihanSoal(idSoal);
        } else {
            Toast.makeText(this, "ID soal tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }

        // Listener untuk tombol Kembali
        btnKembali.setOnClickListener(v -> finish());

        // Listener untuk tombol Mulai
        btnMulai.setOnClickListener(v -> {
            // Buat AlertDialog untuk peringatan
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailLatihanSoalActivity.this);
            builder.setTitle("Peringatan")
                    .setMessage("Anda tidak dapat keluar jika latihan sedang berlangsung. Apakah Anda ingin melanjutkan?")
                    .setCancelable(false) // Membuat dialog tidak dapat ditutup kecuali dengan tombol
                    .setPositiveButton("Ya", (dialog, which) -> {
                        // Lanjutkan ke aktivitas ProsesLatihanSoal
                        Intent intent = new Intent(DetailLatihanSoalActivity.this, ProsesLatihanSoalActivity.class);
                        intent.putExtra("id_latihan_soal", idSoal);
                        startActivityForResult(intent, 1); // Mulai aktivitas dengan result
                    })
                    .setNegativeButton("Tidak", (dialog, which) -> {
                        // Tutup dialog
                        dialog.dismiss();
                    });

            // Tampilkan dialog
            AlertDialog dialog = builder.create();
            dialog.show();

            // Set warna tombol setelah dialog ditampilkan
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.biru_tua));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.biru_tua));
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

    private void getNilaiLatihanSoal(int idSoal) {
        String url = "http://192.168.159.228:8000/api/latihan-soal/" + idSoal + "/nilai";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Periksa status respons
                        if (response.getString("status").equals("success")) {
                            // Akses objek 'data' dan ambil nilai
                            JSONObject dataObj = response.getJSONObject("data");

                            if (dataObj != null) {
                                // Ambil nilai dari key "Nilai"
                                int nilaiSoal = dataObj.getInt("Nilai");

                                // Update UI untuk menampilkan nilai (hanya angka)
                                tvNilai.setText(String.valueOf(nilaiSoal));
                            } else {
                                Toast.makeText(this, "Data nilai tidak ditemukan.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Gagal memuat nilai soal.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Latihan soal belum diselesaikan", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Gagal memuat data nilai.", Toast.LENGTH_SHORT).show()) {
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
