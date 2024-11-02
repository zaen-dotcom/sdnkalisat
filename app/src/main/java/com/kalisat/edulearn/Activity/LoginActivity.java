package com.kalisat.edulearn.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;  // Import Log for debugging
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

    private EditText etEmail, etPassword;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi elemen UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        // Instance Retrofit
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // OnClick Listener untuk tombol Sign In
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Validasi input
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

                // Membuat request login
                LoginRequest loginRequest = new LoginRequest(email, password);

                // Memanggil API login
                Call<LoginResponse> call = apiService.login(loginRequest);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Log.d("LoginActivity", "Response: " + response.body().toString());

                                if (response.body().isStatus()) {
                                    // Menentukan peran berdasarkan role ID
                                    String roleMessage;
                                    int roleId = response.body().getRole();

                                    switch (roleId) {
                                        case 1:
                                            roleMessage = "Admin";
                                            break;
                                        case 2:
                                            roleMessage = "Guru";
                                            break;
                                        case 3:
                                            roleMessage = "Siswa";
                                            break;
                                        default:
                                            roleMessage = "Tidak diketahui";
                                            break;
                                    }

                                    // Menampilkan pesan login berhasil dengan peran yang sesuai
                                    showAlert("Login Berhasil sebagai " + roleMessage, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                } else {
                                    // Login gagal
                                    showAlert("Login Gagal: " + response.body().getMessage());
                                }
                            } else {
                                showAlert("Response body kosong");
                            }
                        } else {
                            try {
                                String errorBody = response.errorBody().string();
                                Log.d("LoginActivity", "Error Body: " + errorBody);
                                showAlert("Response tidak berhasil: " + errorBody);
                            } catch (Exception e) {
                                e.printStackTrace();
                                showAlert("Error saat mengambil data");
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        showAlert("Gagal: " + t.getMessage());
                        Log.e("LoginActivity", "Failure: ", t);
                    }
                });
            }
        });
    }

    // Method untuk menampilkan AlertDialog
    private void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // Overloaded method untuk alert dengan listener
    private void showAlert(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .show();
    }
}
