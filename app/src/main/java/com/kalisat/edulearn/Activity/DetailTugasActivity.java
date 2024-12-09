package com.kalisat.edulearn.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kalisat.edulearn.R;
import com.kalisat.edulearn.Utils.ImageUtil;
import com.kalisat.edulearn.Utils.SharedPreferencesUtil;  // Import util yang sudah kita buat

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailTugasActivity extends AppCompatActivity {

    private TextView tvJudulTgs, tvTenggat, tvDeskripsi, tvNilai;
    private LinearLayout linearLayoutThumbnails;
    private Button btnKirim;
    private int id;
    private List<Uri> imageUris = new ArrayList<>();
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tugas);

        initUI();
        requestQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        if (id == -1) {
            showErrorAndExit("ID tidak valid!");
            return;
        }

        getDataFromAPI();
        getNilaiTugas();
    }

    private void initUI() {
        tvJudulTgs = findViewById(R.id.tv_judultgs);
        tvTenggat = findViewById(R.id.tv_tenggat);
        tvDeskripsi = findViewById(R.id.tv_deskripsi);
        tvNilai = findViewById(R.id.tv_nilai);
        linearLayoutThumbnails = findViewById(R.id.linearLayoutThumbnails);
        btnKirim = findViewById(R.id.btn_kirim);

        findViewById(R.id.ic_kembali).setOnClickListener(v -> finish());
        findViewById(R.id.uploadtugas).setOnClickListener(v -> openGallery());
        btnKirim.setOnClickListener(v -> submitTask());
    }

    private void getDataFromAPI() {
        String url = "http://192.168.159.228:8000/api/tugas/" + id;
        Map<String, String> headers = getHeaders();

        ProgressDialog progressDialog = createProgressDialog("Memuat data tugas...");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    progressDialog.dismiss();
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONObject data = response.getJSONArray("data").getJSONObject(0);
                            tvJudulTgs.setText(data.optString("judul_tugas", "Judul tidak tersedia"));
                            tvTenggat.setText("Deadline: " + data.optString("deadline", "Tidak ada deadline"));
                            tvDeskripsi.setText(data.optString("deskripsi", "Deskripsi tidak tersedia"));
                        } else {
                            showError("Gagal mengambil data tugas.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showError(e.getMessage());
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    showError(error.getMessage());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void showExistingFoto(String fotoBase64) {
        if (fotoBase64 == null || fotoBase64.isEmpty()) {
            showError("Foto tidak tersedia.");
            return;
        }

        try {
            String base64Image = "data:image/png;base64," + fotoBase64;

            runOnUiThread(() -> {
                View itemView = getLayoutInflater().inflate(R.layout.item_pilihfoto, linearLayoutThumbnails, false);
                ImageView imageThumbnail = itemView.findViewById(R.id.tempatthumbnail);

                Glide.with(DetailTugasActivity.this)
                        .load(base64Image)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageThumbnail);

                ImageView imageClose = itemView.findViewById(R.id.ic_remove);
                imageClose.setVisibility(View.GONE);

                linearLayoutThumbnails.addView(itemView);
            });

        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal menampilkan foto.");
        }
    }


    private void getNilaiTugas() {
        String url = "http://192.168.159.228:8000/api/tugas/" + id + "/nilai";
        Map<String, String> headers = getHeaders();

        ProgressDialog progressDialog = createProgressDialog("Memuat nilai...");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    progressDialog.dismiss();
                    try {
                        if (response.getString("status").equals("success")) {
                            // Ambil data JSON
                            JSONObject data = response.optJSONObject("data");

                            if (data == null) {
                                // Jika data null, tampilkan toast
                                Toast.makeText(DetailTugasActivity.this, "Anda belum menyelesaikan tugas ini", Toast.LENGTH_SHORT).show();
                                tvNilai.setText("Nilai: Tidak ada");
                                return;
                            }

                            // Ambil nilai grade
                            String nilai = data.optString("grade", "Belum dinilai");
                            tvNilai.setText("Nilai: " + nilai);

                            // Ambil foto base64
                            String fotoBase64 = data.optString("foto", null);
                            String savedFotoBase64 = SharedPreferencesUtil.getSavedImageFromPreferences(this);

                            if (fotoBase64 != null && !fotoBase64.isEmpty()) {
                                if (!fotoBase64.equals(savedFotoBase64)) {
                                    SharedPreferencesUtil.saveImageToPreferences(this, fotoBase64);
                                    showExistingFoto(fotoBase64);
                                } else {
                                    showExistingFoto(savedFotoBase64);
                                }
                            }
                        } else {
                            showError("Gagal mendapatkan nilai tugas.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showError(e.getMessage());
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    showError(error.getMessage());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }



    private void loadSavedFoto() {
        String savedFotoBase64 = SharedPreferencesUtil.getSavedImageFromPreferences(this);
        if (savedFotoBase64 != null && !savedFotoBase64.isEmpty()) {
            showExistingFoto(savedFotoBase64);  // Menampilkan foto dari SharedPreferences
        }
    }


    private void submitTask() {
        if (imageUris.isEmpty()) {
            showError("Silahkan upload foto terlebih dahulu.");
            return;
        }

        String url = "http://192.168.159.228:8000/api/submit-tugas";
        Map<String, String> headers = getHeaders();
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("id_tugas", id);
            jsonParams.put("foto", ImageUtil.compressImageAndConvertToBase64(this, imageUris.get(0)));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                response -> {
                    Toast.makeText(DetailTugasActivity.this, "Tugas berhasil dikirim!", Toast.LENGTH_SHORT).show();
                    // Simpan foto yang baru dikirim ke SharedPreferences
                    String fotoBase64 = ImageUtil.compressImageAndConvertToBase64(this, imageUris.get(0));
                    SharedPreferencesUtil.saveImageToPreferences(this, fotoBase64);


                    recreate();
                    loadSavedFoto();
                },
                error -> showError("Gagal mengirim tugas.")
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                if (imageUris.size() >= 1) {
                    // Ganti Toast dengan AlertDialog
                    showAlertDialog("Anda hanya bisa mengirim 1 foto.");
                    return;
                }

                imageUris.add(imageUri);
                View itemView = getLayoutInflater().inflate(R.layout.item_pilihfoto, linearLayoutThumbnails, false);
                ImageView imageThumbnail = itemView.findViewById(R.id.tempatthumbnail);
                imageThumbnail.setImageURI(imageUri);

                ImageView imageClose = itemView.findViewById(R.id.ic_remove);
                imageClose.setOnClickListener(v -> {
                    linearLayoutThumbnails.removeView(itemView);
                    imageUris.remove(imageUri);
                });

                linearLayoutThumbnails.addView(itemView);
            }
        }
    }

    private void showAlertDialog(String message) {
        // Membuat AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Peringatan")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());


        AlertDialog dialog = builder.create();
        dialog.show();


        Button okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (okButton != null) {
            okButton.setTextColor(getResources().getColor(R.color.biru_tua));
        }
    }


    private Map<String, String> getHeaders() {
        String token = getSharedPreferences("user_session", MODE_PRIVATE).getString("user_token", "");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Accept", "application/json");
        return headers;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private ProgressDialog createProgressDialog(String message) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorAndExit(String message) {
        showError(message);
        finish();
    }
}
