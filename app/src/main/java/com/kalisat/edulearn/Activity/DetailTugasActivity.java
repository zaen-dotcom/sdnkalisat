package com.kalisat.edulearn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailTugasActivity extends AppCompatActivity {

    private TextView tvJudulTgs, tvTenggat, tvDeskripsi;
    private ImageView icKembali;
    private int id; // Menggunakan id, bukan idMapel
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tugas);

        // Inisialisasi UI
        tvJudulTgs = findViewById(R.id.tv_judultgs);
        tvTenggat = findViewById(R.id.tv_tenggat);
        tvDeskripsi = findViewById(R.id.tv_deskripsi);
        icKembali = findViewById(R.id.ic_kembali);

        // Inisialisasi Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Ambil data dari Intent
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1); // Ganti ke "id"

        // Debug ID
        Log.d("DetailTugasActivity", "ID diterima: " + id);

        // Tombol kembali
        icKembali.setOnClickListener(v -> finish());

        // Validasi ID
        if (id == -1) {
            Log.e("DetailTugasActivity", "ID tidak valid, tidak ada di Intent.");
            Toast.makeText(this, "ID tidak valid!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Panggil API untuk mendapatkan data tugas
        getDataFromAPI();
    }

    private void getDataFromAPI() {
        // Gunakan id untuk membentuk URL
        String url = "http://192.168.159.228:8000/api/tugas/" + id;

        // Ambil token dari SharedPreferences
        String token = getSharedPreferences("user_session", MODE_PRIVATE).getString("user_token", "");

        // Debug token dan URL
        Log.d("DetailTugasActivity", "Token: " + token);
        Log.d("DetailTugasActivity", "URL API: " + url);

        // Jika token tidak tersedia
        if (token.isEmpty()) {
            Toast.makeText(this, "Token tidak tersedia. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("DetailTugasActivity", "Respons API: " + response.toString());
                            if (response.getString("status").equals("success")) {
                                JSONArray dataArray = response.getJSONArray("data");

                                // Validasi jika data kosong
                                if (dataArray.length() == 0) {
                                    Toast.makeText(DetailTugasActivity.this, "Data tugas tidak ditemukan.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    return;
                                }

                                // Ambil data pertama dari array
                                JSONObject data = dataArray.getJSONObject(0);

                                // Ambil data dari API
                                String judulTugas = data.optString("judul_tugas", "Judul tidak tersedia");
                                String tenggatWaktu = data.optString("deadline", "Tidak ada deadline");
                                String deskripsi = data.optString("deskripsi", "Deskripsi tidak tersedia");

                                // Set data ke UI
                                tvJudulTgs.setText(judulTugas);
                                tvTenggat.setText("Deadline: " + tenggatWaktu);
                                tvDeskripsi.setText(deskripsi);
                            } else {
                                Toast.makeText(DetailTugasActivity.this, "Gagal mengambil data tugas.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailTugasActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        String errorMsg = "Kesalahan koneksi.";
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            errorMsg += " Respons: " + new String(error.networkResponse.data);
                        }
                        Toast.makeText(DetailTugasActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        Log.e("DetailTugasActivity", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        // Tambahkan request ke RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}
