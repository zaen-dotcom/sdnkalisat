package com.kalisat.edulearn.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kalisat.edulearn.Model.ChangePasswordRequest;
import com.kalisat.edulearn.Model.ChangePasswordResponse;
import com.kalisat.edulearn.R;
import com.kalisat.edulearn.Network.ApiClient;
import com.kalisat.edulearn.Network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahSandiActivity extends AppCompatActivity {

    private EditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubahsandi);

        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtconfirmPassword);

        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        findViewById(R.id.btnkonfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String currentPassword = edtCurrentPassword.getText().toString();
        String newPassword = edtNewPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            showAlert("Cek Kembali", "Semua kolom harus diisi", false);
            return;
        }

        // Validasi panjang password baru
        if (newPassword.length() < 8) {
            showAlert("Perhatian", "Password baru minimal 8 karakter.", false);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Cek Kembali", "Password baru dan konfirmasi password tidak sesuai.", false);
            return;
        }

        int userId = 1; // Ganti ini dengan user ID yang benar dari sesi atau intent
        ChangePasswordRequest request = new ChangePasswordRequest(userId, currentPassword, newPassword);

        apiService.changePassword(request).enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChangePasswordResponse changePasswordResponse = response.body();
                    if (changePasswordResponse.isStatus()) {
                        showAlert("Success", changePasswordResponse.getMessage(), true);
                    } else {
                        showAlert("Error", "Password lama/saat ini tidak sesuai", false);
                    }
                } else {
                    showAlert("Error", "Terjadi kesalahan pada server", false);
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                showAlert("Error", "Gagal terhubung ke server", false);
            }
        });
    }

    private void showAlert(String title, String message, boolean shouldFinish) {
        if (isFinishing() || isDestroyed()) {
            return; // Jangan tampilkan dialog jika Activity sudah selesai atau dihancurkan
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    if (shouldFinish) {
                        finish(); // Menutup Activity setelah dialog ditutup jika operasi berhasil
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Nonaktifkan penutupan dialog ketika di luar area dialog atau tombol "Back" ditekan
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        // Mengubah warna tombol OK menjadi warna biru setelah dialog ditampilkan
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.biru_tua));
    }
}
