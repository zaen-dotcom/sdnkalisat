package com.kalisat.edulearn.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Adapter.LatihanSoalPagerAdapter;
import com.kalisat.edulearn.Model.ModelSoal;
import com.kalisat.edulearn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProsesLatihanSoalActivity extends AppCompatActivity {

    private ViewPager2 viewPagerSoal;
    private LatihanSoalPagerAdapter adapter;
    private List<ModelSoal> soalList;
    private int idLatihanSoal;
    private String bearerToken;
    private ImageView icNextPage, icBackPage;
    private Button btnSelesai; // Tambahkan tombol selesai

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_latihan_soal);

        // Inisialisasi ViewPager2
        viewPagerSoal = findViewById(R.id.viewPagerSoal);

        // Inisialisasi Elemen Navigasi
        icNextPage = findViewById(R.id.ic_nextpage);
        icBackPage = findViewById(R.id.ic_backpage);
        btnSelesai = findViewById(R.id.btn_selesai); // Inisialisasi tombol selesai

        // Tombol selesai disembunyikan di awal
        btnSelesai.setVisibility(View.GONE);

        // Log untuk memeriksa apakah elemen-elemen navigasi terinisialisasi dengan benar
        Log.d("UI Check", "icNextPage: " + icNextPage + " icBackPage: " + icBackPage);

        // Ambil ID latihan soal dari Intent
        idLatihanSoal = getIntent().getIntExtra("id_latihan_soal", -1);

        // Ambil Bearer Token dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        bearerToken = sharedPreferences.getString("user_token", "");

        if (idLatihanSoal != -1 && !bearerToken.isEmpty()) {
            // Inisialisasi list soal
            soalList = new ArrayList<>();
            // Panggil API untuk mendapatkan data soal
            getLatihanSoal(1); // Mulai dari halaman 1
        } else {
            Toast.makeText(this, "ID soal atau token tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Log untuk memeriksa apakah idLatihanSoal dan bearerToken sudah benar
        Log.d("UI Check", "idLatihanSoal: " + idLatihanSoal + " bearerToken: " + bearerToken);
    }

    private void getLatihanSoal(int page) {
        String url = "http://192.168.159.228:8000/api/latihan-soal/" + idLatihanSoal + "/process?page=" + page;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject dataObject = response.getJSONObject("data");
                        JSONArray dataArray = dataObject.getJSONArray("data");
                        int currentPage = dataObject.getInt("current_page");
                        int lastPage = dataObject.getInt("last_page");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject soalObj = dataArray.getJSONObject(i);

                            int id = soalObj.getInt("id");
                            String soal = soalObj.getString("soal");
                            String pilihanA = soalObj.getString("a");
                            String pilihanB = soalObj.getString("b");
                            String pilihanC = soalObj.getString("c");
                            String pilihanD = soalObj.getString("d");
                            String jawaban = soalObj.getString("jawaban");

                            // Tambahkan soal ke list
                            soalList.add(new ModelSoal(id, soal, pilihanA, pilihanB, pilihanC, pilihanD, jawaban));
                        }

                        // Atur adapter untuk ViewPager2 setelah data dimuat
                        setupViewPager();

                        // Jika masih ada halaman berikutnya, panggil lagi
                        if (currentPage < lastPage) {
                            getLatihanSoal(currentPage + 1);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Kesalahan parsing data.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Gagal terhubung ke server. Periksa koneksi Anda.", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + bearerToken);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        // Tambahkan request ke Volley RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void setupViewPager() {
        // Cek apakah soalList sudah berisi data
        if (soalList.isEmpty()) {
            Log.e("SetupViewPager", "soalList is empty, cannot set adapter.");
            return;
        }

        adapter = new LatihanSoalPagerAdapter(soalList);
        viewPagerSoal.setAdapter(adapter);

        // Log untuk memeriksa apakah adapter telah di-set
        Log.d("UI Check", "Adapter set to ViewPager2");

        // Navigasi Next page
        icNextPage.setOnClickListener(v -> {
            if (viewPagerSoal.getCurrentItem() < soalList.size() - 1) {
                viewPagerSoal.setCurrentItem(viewPagerSoal.getCurrentItem() + 1);
            }
        });

        // Navigasi Back page
        icBackPage.setOnClickListener(v -> {
            if (viewPagerSoal.getCurrentItem() > 0) {
                viewPagerSoal.setCurrentItem(viewPagerSoal.getCurrentItem() - 1);
            }
        });

        // Tambahkan listener untuk mendeteksi perubahan halaman
        viewPagerSoal.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == soalList.size() - 1) {
                    // Halaman terakhir, sembunyikan tombol next dan tampilkan tombol selesai
                    icNextPage.setVisibility(View.GONE);
                    btnSelesai.setVisibility(View.VISIBLE);
                } else {
                    // Bukan halaman terakhir, tampilkan tombol next dan sembunyikan tombol selesai
                    icNextPage.setVisibility(View.VISIBLE);
                    btnSelesai.setVisibility(View.GONE);
                }
            }
        });

        // Tambahkan aksi untuk tombol selesai
        btnSelesai.setOnClickListener(v -> {
            Toast.makeText(ProsesLatihanSoalActivity.this, "Latihan selesai!", Toast.LENGTH_SHORT).show();
            finish(); // Akhiri aktivitas atau navigasikan ke halaman lain
        });
    }
}
