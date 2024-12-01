package com.kalisat.edulearn.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Fragment.ProfileFragment;
import com.kalisat.edulearn.R;

import java.util.HashMap;
import java.util.Map;

public class UbahSandiActivity extends AppCompatActivity {

    private EditText edtCurrentPassword, edtPassword, edtConfirmPassword;
    private Button btnKonfirm;
    private ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubahsandi);

        // Inisialisasi komponen UI
        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtconfirmPassword);
        btnKonfirm = findViewById(R.id.btnkonfirm);
        backIcon = findViewById(R.id.back_icon);

        // Tambahkan listener untuk kembali ke aktivitas sebelumnya
        backIcon.setOnClickListener(v -> finish());

        // Listener untuk tombol Konfirmasi
        btnKonfirm.setOnClickListener(v -> updatePassword());
    }

    private void updatePassword() {
        String currentPassword = edtCurrentPassword.getText().toString();
        String newPassword = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        // Validasi input
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Peringatan", "Semua kolom harus diisi");
            return;
        }

        if (newPassword.length() < 8 || confirmPassword.length() < 8) {
            showAlert("Peringatan", "Kata sandi baru minimal 8 karakter");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Peringatan", "Kata sandi baru dan konfirmasi tidak cocok");
            return;
        }

        // Ambil token Bearer dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", "");  // Mengambil token yang disimpan

        if (token.isEmpty()) {
            showAlert("Peringatan", "Token tidak ditemukan. Silakan login kembali.");
            return;
        }

        String url = "http://192.168.159.228:8000/api/update-password";

        // Buat request untuk mengganti password
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tangani response dari server
                        Toast.makeText(UbahSandiActivity.this, "Kata sandi berhasil diubah", Toast.LENGTH_SHORT).show();

                        // Pindah ke ProfileFragment setelah password berhasil diubah
                        navigateToProfileFragment();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String errorMessage = "Kata sandi saat ini tidak cocok.";


                        if (error.getMessage() != null) {
                            errorMessage += " " + error.getMessage();
                        }

                        showAlert("Kesalahan", errorMessage);
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Kirim data untuk mengganti password
                Map<String, String> params = new HashMap<>();
                params.put("password_lama", currentPassword);
                params.put("password_baru", newPassword);
                params.put("confirm_password", confirmPassword);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        // Menambahkan request ke queue Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // Fungsi untuk menampilkan alert dialog dengan tombol OK berwarna biru
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)  // Membuat dialog tidak bisa ditutup kecuali dengan tombol OK
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();  // Menutup dialog setelah tombol OK ditekan
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Mengubah warna tombol OK menjadi biru
        Button okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setTextColor(ContextCompat.getColor(this, R.color.biru_tua)); // Menggunakan warna biru
    }

    // Fungsi untuk navigasi ke ProfileFragment
    private void navigateToProfileFragment() {
        ProfileFragment profileFragment = new ProfileFragment();

        // Menggunakan FragmentTransaction untuk mengganti konten layar dengan ProfileFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, profileFragment);  // Pastikan ID container sesuai dengan layout fragment
        transaction.addToBackStack(null);  // Menambahkan ke backstack agar pengguna bisa kembali ke activity sebelumnya
        transaction.commit();
    }
}
