package com.kalisat.edulearn.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
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
    private Button btnSelesai; // Tombol selesai

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

        // Tangani tombol back agar dinonaktifkan
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Tampilkan pesan jika tombol back ditekan
                Toast.makeText(ProsesLatihanSoalActivity.this, "Kembali tidak diizinkan saat mengerjakan soal.", Toast.LENGTH_SHORT).show();
            }
        });
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

        // Listener untuk perubahan halaman
        viewPagerSoal.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == soalList.size() - 1) {
                    icNextPage.setVisibility(View.GONE);
                    btnSelesai.setVisibility(View.VISIBLE);
                } else {
                    icNextPage.setVisibility(View.VISIBLE);
                    btnSelesai.setVisibility(View.GONE);
                }
            }
        });

        // Aksi tombol selesai
        btnSelesai.setOnClickListener(v -> {
            // Validasi apakah semua soal telah dijawab
            boolean allAnswered = true;
            for (ModelSoal soal : soalList) {
                if (soal.getSelectedAnswer() == null) {
                    allAnswered = false;
                    break;
                }
            }

            if (allAnswered) {
                showConfirmationDialog();
            } else {
                showIncompleteAnswersDialog(); // Panggil dialog custom untuk validasi jawaban
            }
        });
    }

    private void showIncompleteAnswersDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Peringatan");
        builder.setMessage("Harap jawab semua soal sebelum mengirim.");

        // Tombol OK untuk menutup dialog
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        // Buat dialog non-cancellable
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false); // Mencegah dialog ditutup dengan tombol back
        dialog.setCanceledOnTouchOutside(false); // Mencegah ditutup dengan menyentuh luar dialog

        // Tampilkan dialog terlebih dahulu
        dialog.show();

        // Ubah warna tombol setelah dialog ditampilkan
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.biru_tua));
    }


    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Selesai");
        builder.setMessage("Kirim untuk menyelesaikan latihan soal.");

        // Tombol Kirim
        builder.setPositiveButton("Kirim", (dialog, which) -> {
            submitAnswersToServer();
        });

        // Tombol Kembali
        builder.setNegativeButton("Kembali", (dialog, which) -> {
            dialog.dismiss();
        });

        // Buat dialog non-cancellable
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false); // Mencegah ditutup dengan tombol back
        dialog.setCanceledOnTouchOutside(false); // Mencegah ditutup dengan menyentuh luar dialog

        // Tampilkan dialog terlebih dahulu
        dialog.show();

        // Ubah warna tombol setelah dialog ditampilkan
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.biru_tua));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.biru_tua));
    }




    private void submitAnswersToServer() {
        String url = "http://192.168.159.228:8000/api/submit-latihan-soal";

        JSONObject payload = new JSONObject();
        try {
            payload.put("id_siswa", 1);
            JSONArray answersArray = new JSONArray();

            for (ModelSoal soal : soalList) {
                if (soal.getSelectedAnswer() != null) {
                    JSONObject answerObject = new JSONObject();
                    answerObject.put("id_latihan_soal", soal.getId());
                    answerObject.put("jawaban", soal.getSelectedAnswer());
                    answersArray.put(answerObject);
                }
            }

            payload.put("answer", answersArray);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Kesalahan saat mempersiapkan data.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, payload,
                response -> {
                    // Simpan status soal telah dikerjakan
                    SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("soal_dikerjakan_" + idLatihanSoal, true); // Tandai soal telah dikerjakan
                    editor.apply();

                    Toast.makeText(this, "Jawaban berhasil dikirim!", Toast.LENGTH_SHORT).show();

                    // Kirim hasil kembali ke DetailLatihanSoalActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("result", "completed");
                    setResult(RESULT_OK, resultIntent);
                    finish();
                },
                error -> Toast.makeText(this, "Gagal mengirim jawaban.", Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + bearerToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }



    @Override
    public void onBackPressed() {
        Log.d("ProsesLatihanSoal", "onBackPressed dipanggil");
        Toast.makeText(this, "Kembali tidak diizinkan saat mengerjakan soal.", Toast.LENGTH_SHORT).show();
    }

}
