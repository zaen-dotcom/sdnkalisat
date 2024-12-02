package com.kalisat.edulearn.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailTugasActivity extends AppCompatActivity {

    private TextView tvJudulTgs, tvTenggat, tvDeskripsi;
    private ImageView icKembali;
    private Button btnKirim;
    private int id;
    private LinearLayout linearLayoutThumbnails;
    private static final int PICK_IMAGE_REQUEST = 1;
    private List<Uri> imageUris = new ArrayList<>();
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
        btnKirim = findViewById(R.id.btn_kirim);
        linearLayoutThumbnails = findViewById(R.id.linearLayoutThumbnails);

        // Tambahkan ini untuk TextView nilai tugas
        TextView tvNilai = findViewById(R.id.tv_nilai);

        requestQueue = Volley.newRequestQueue(this);

        // Tombol kembali
        icKembali.setOnClickListener(v -> finish());

        // Ambil data dari Intent
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        if (id == -1) {
            Log.e("DetailTugasActivity", "ID tidak valid, tidak ada di Intent.");
            Toast.makeText(this, "ID tidak valid!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Panggil API untuk mendapatkan data tugas
        getDataFromAPI();

        // Tambahkan panggilan API untuk getNilaiTugas
        getNilaiTugas(tvNilai);

        // Tombol upload tugas (pilih gambar)
        Button btnUploadTugas = findViewById(R.id.uploadtugas);
        btnUploadTugas.setOnClickListener(v -> openGallery());

        // Tombol Kirim untuk mengirim tugas
        btnKirim.setOnClickListener(v -> submitTask(v));
    }

    private void getNilaiTugas(TextView tvNilai) {
        String url = "http://192.168.159.228:8000/api/tugas/" + id + "/nilai";
        String token = getSharedPreferences("user_session", MODE_PRIVATE).getString("user_token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Token tidak tersedia. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONObject data = response.getJSONObject("data");

                            // Ambil nilai dari respon
                            String nilai = data.optString("grade", "Belum dinilai");

                            // Tampilkan nilai pada TextView
                            tvNilai.setText("Nilai: " + nilai);

                            // Menggunakan layout item_pilihfoto untuk menampilkan thumbnail foto
                            View itemView = getLayoutInflater().inflate(R.layout.item_pilihfoto, linearLayoutThumbnails, false);

                            // Set thumbnail atau placeholder pada ImageView
                            ImageView imageThumbnail = itemView.findViewById(R.id.tempatthumbnail);
                            // Gunakan gambar placeholder atau ikon default, misalnya:
                            imageThumbnail.setImageResource(R.drawable.ic_image);  // Ganti dengan gambar placeholder yang sesuai

                            // Sembunyikan tombol X karena foto berasal dari API dan tidak akan dihapus
                            ImageView imageClose = itemView.findViewById(R.id.ic_remove);
                            imageClose.setVisibility(View.GONE);

                            // Tambahkan ke layout
                            linearLayoutThumbnails.addView(itemView);
                        } else {
                            Toast.makeText(this, "Gagal mendapatkan nilai tugas.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
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

        // Request data tugas
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(DetailTugasActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(DetailTugasActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
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
            // Mendapatkan URI gambar yang dipilih
            Uri imageUri = data.getData();

            // Cek apakah sudah ada gambar yang dipilih sebelumnya
            if (imageUris.size() >= 1) {
                showAlertDialog("Peringatan", "Anda hanya bisa mengirim 1 foto.");
                return;
            }

            // Menambahkan foto ke list imageUris
            imageUris.add(imageUri);

            // Membuat layout baru untuk thumbnail foto dan tombol X
            View itemView = getLayoutInflater().inflate(R.layout.item_pilihfoto, linearLayoutThumbnails, false);

            // Menampilkan gambar thumbnail
            ImageView imageThumbnail = itemView.findViewById(R.id.tempatthumbnail);
            imageThumbnail.setImageURI(imageUri);

            // Menambahkan event listener untuk tombol X
            ImageView imageClose = itemView.findViewById(R.id.ic_remove);
            imageClose.setOnClickListener(v -> {
                // Menghapus item foto ketika tombol X ditekan
                linearLayoutThumbnails.removeView(itemView);
                imageUris.remove(imageUri); // Menghapus foto dari list
            });

            // Menambahkan item (foto + tombol X) ke LinearLayout
            linearLayoutThumbnails.addView(itemView);
        }
    }

    public void submitTask(View view) {
        if (imageUris.isEmpty()) {
            showAlertDialog("Peringatan", "Silahkan upload foto terlebih dahulu.");
            return;
        }

        // Kompres gambar sebelum mengonversinya ke Base64
        StringBuilder base64Images = new StringBuilder();

        for (Uri uri : imageUris) {
            String base64Image = compressImageAndConvertToBase64(uri);
            if (base64Image != null) {
                base64Images.append(base64Image).append(",");
            }
        }

        // Menghapus tanda koma terakhir
        if (base64Images.length() > 0) {
            base64Images.deleteCharAt(base64Images.length() - 1);
        }

        String url = "http://192.168.159.228:8000/api/submit-tugas";
        String token = getSharedPreferences("user_session", MODE_PRIVATE).getString("user_token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Token tidak tersedia. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("id_tugas", id);
            jsonParams.put("foto", base64Images.toString()); // Mengirimkan foto dalam format Base64
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                response -> {
                    try {
                        Log.d("API Response", response.toString());
                        if (response.getString("status").equals("success")) {
                            showAlertDialog("Berhasil", "Tugas berhasil di-submit.");
                        } else {
                            showAlertDialog("Gagal", "Gagal mengirim tugas. Silakan coba lagi.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showAlertDialog("Error", "Terjadi kesalahan: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("API Error", error.getMessage());
                    showAlertDialog("Perhatian", "Terjadi kesalahan saat mengirim tugas.");
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

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

    private String compressImageAndConvertToBase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);

            int maxWidth = 1024;
            int maxHeight = 1024;
            int width = originalBitmap.getWidth();
            int height = originalBitmap.getHeight();
            float scaleFactor = Math.min((float) maxWidth / width, (float) maxHeight / height);

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap,
                    (int) (width * scaleFactor),
                    (int) (height * scaleFactor),
                    false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 65, baos);

            byte[] byteArray = baos.toByteArray();

            return Base64.encodeToString(byteArray, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}