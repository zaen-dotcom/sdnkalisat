package com.kalisat.edulearn.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kalisat.edulearn.R;
import com.kalisat.edulearn.Model.LoginRequest;
import com.kalisat.edulearn.Model.LoginResponse;
import com.kalisat.edulearn.Network.ApiClient;
import com.kalisat.edulearn.Network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Mengecek apakah pengguna sudah login sebelumnya
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Inisialisasi komponen UI
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnSignIn = findViewById(R.id.btnSignIn);

        // Inisialisasi ApiService
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        btnSignIn.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) {
                showAlert("Email harus diisi");
                etEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                showAlert("Password harus diisi");
                etPassword.requestFocus();
                return;
            }

            // Membuat objek LoginRequest dan memanggil API login
            LoginRequest loginRequest = new LoginRequest(email, password);
            Call<LoginResponse> call = apiService.login(loginRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                        // Menyimpan informasi pengguna ke SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putInt("user_id", response.body().getUser().getId());
                        editor.putString("nama", response.body().getUser().getNama());
                        editor.putString("role", response.body().getRole());
                        editor.apply();

                        // Menampilkan alert login berhasil
                        showAlert("Login Berhasil sebagai " + response.body().getRole(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        // Menampilkan pesan jika login gagal
                        showAlert("Login Gagal: " + (response.body() != null ? response.body().getMessage() : "Kesalahan"));
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    showAlert("Gagal: " + t.getMessage());
                    Log.e("LoginActivity", "Failure: ", t);
                }
            });
        });
    }

    // Menampilkan AlertDialog dengan pesan sederhana
    private void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // Overloaded showAlert dengan listener untuk menangani tindakan OK
    private void showAlert(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .show();
    }
}
