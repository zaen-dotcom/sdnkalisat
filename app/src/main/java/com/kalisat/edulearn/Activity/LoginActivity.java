package com.kalisat.edulearn.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.R;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etIdentitas, etPassword;
    private Button btnSignIn;
    private TextView hubAdmin; // Tambahkan variabel untuk TextView hubadmin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etIdentitas = findViewById(R.id.etIdentitas);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        hubAdmin = findViewById(R.id.hubadmin); 


        hubAdmin.setOnClickListener(v -> {
            // Nomor WhatsApp yang akan dihubungi
            String phoneNumber = "082139512292";
            String message = "Halo, saya membutuhkan bantuan.";
            openWhatsApp(phoneNumber, message);
        });

        btnSignIn.setOnClickListener(v -> {
            String nisn = etIdentitas.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (nisn.isEmpty() || password.isEmpty()) {
                showAlert("Peringatan", "NISN dan Password tidak boleh kosong!");
            } else {
                login(nisn, password);
            }
        });
    }

    private void openWhatsApp(String phoneNumber, String message) {
        String url = "https://wa.me/" + phoneNumber + "?text=" + message;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse(url));
        startActivity(intent);
    }

    private void login(String nisn, String password) {
        String url = "http://192.168.159.228:8000/api/login";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("nisn", nisn);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            String token = response.getJSONObject("data").getString("token");

                            SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
                            sharedPreferences.edit().putString("user_token", token).apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user_token", token);
                            startActivity(intent);
                            finish();
                        } else {
                            showAlert("Login Gagal", response.getString("message"));
                        }
                    } catch (JSONException e) {
                        showAlert("Kesalahan", "Kesalahan parsing data, mohon coba lagi.");
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                        showAlert("Login Gagal", "Username atau password salah.");
                    } else {
                        showAlert("Kesalahan", "Gagal terhubung ke server, pastikan koneksi Anda stabil.");
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void showAlert(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();

        // Mengubah warna teks tombol positif (OK)
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.biru_tua));  // Menggunakan warna dari resource
    }
}
