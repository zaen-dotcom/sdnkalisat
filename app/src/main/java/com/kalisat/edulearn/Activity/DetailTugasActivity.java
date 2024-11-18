package com.kalisat.edulearn.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import android.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class DetailTugasActivity extends AppCompatActivity {

    private TextView tvJudulTgs, tvTenggat, tvDeskripsi, tvUpload;
    private ImageView icKembali;
    private Button btnKirim;
    private int id;
    private RequestQueue requestQueue;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tugas);

        // Inisialisasi UI
        tvJudulTgs = findViewById(R.id.tv_judultgs);
        tvTenggat = findViewById(R.id.tv_tenggat);
        tvDeskripsi = findViewById(R.id.tv_deskripsi);
        tvUpload = findViewById(R.id.tv_upload);
        icKembali = findViewById(R.id.ic_kembali);
        btnKirim = findViewById(R.id.btn_kirim);

        // Inisialisasi Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Ambil data dari Intent
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

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

        // Tombol untuk upload tugas (pilih gambar)
        CardView uploadCardView = findViewById(R.id.uploadtugas);
        uploadCardView.setOnClickListener(v -> openGallery());

        // Tombol Kirim untuk mengirim tugas
        btnKirim.setOnClickListener(v -> submitTask(v));
    }

    private void getDataFromAPI() {
        String url = "http://192.168.159.228:8000/api/tugas/" + id;

        String token = getSharedPreferences("user_session", MODE_PRIVATE).getString("user_token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Token tidak tersedia. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("DetailTugasActivity", "Token yang dikirim untuk GET API: " + token);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("DetailTugasActivity", "Respons API: " + response.toString());
                            if (response.getString("status").equals("success")) {
                                JSONObject data = response.getJSONArray("data").getJSONObject(0);

                                String judulTugas = data.optString("judul_tugas", "Judul tidak tersedia");
                                String tenggatWaktu = data.optString("deadline", "Tidak ada deadline");
                                String deskripsi = data.optString("deskripsi", "Deskripsi tidak tersedia");

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
                        Toast.makeText(DetailTugasActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                Log.d("DetailTugasActivity", "Headers: " + headers.toString());
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void openGallery() {
        // Membuka galeri untuk memilih gambar
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // Hanya memilih file gambar
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            String fileName = getFileName(imageUri);
            String newFileName = "Tugas_" + System.currentTimeMillis() + "_" + fileName;
            tvUpload.setText("Foto telah diupload: " + newFileName);
        }
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        ContentResolver contentResolver = getContentResolver();

        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    fileName = cursor.getString(nameIndex);
                }
            }
        } else if (uri.getScheme().equals("file")) {
            fileName = new File(uri.getPath()).getName();
        }
        return fileName;
    }

    // Fungsi untuk mengirim tugas (upload foto dan data tugas)
    public void submitTask(View view) {
        // Cek apakah foto sudah diupload
        if (imageUri == null) {
            // Jika foto belum diupload, tampilkan alert
            showAlertDialog("Error", "Silakan upload foto terlebih dahulu.");
            return;
        }

        String base64Image = convertImageToBase64(imageUri);

        String url = "http://192.168.159.228:8000/api/submit-tugas";
        String token = getSharedPreferences("user_session", MODE_PRIVATE).getString("user_token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Token tidak tersedia. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("id_tugas", id);
            jsonParams.put("foto", base64Image);
            Log.d("SubmitTask", "Body JSON yang dikirim: " + jsonParams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("SubmitTask", "Respons dari server: " + response.toString());
                            if (response.getString("status").equals("success")) {
                                showAlertDialog("Berhasil", "Tugas berhasil di-submit.");
                            } else {
                                showAlertDialog("Gagal", "Gagal mengirim tugas. Silakan coba lagi.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showAlertDialog("Error", "Terjadi kesalahan: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SubmitTask", "Volley Error: " + error.getMessage());
                        if (error.networkResponse != null) {
                            Log.e("SubmitTask", "Respons error dari server: " + new String(error.networkResponse.data));
                        }
                        showAlertDialog("Error", "Gagal mengirim tugas. Pastikan Anda terhubung ke internet.");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                Log.d("SubmitTask", "Headers: " + headers.toString());
                return headers;
            }
        };

        Log.d("SubmitTask", "Mengirim data ke URL: " + url);
        requestQueue.add(jsonObjectRequest);
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private String convertImageToBase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
