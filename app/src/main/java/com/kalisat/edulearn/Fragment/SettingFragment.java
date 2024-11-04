package com.kalisat.edulearn.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.kalisat.edulearn.Activity.LoginActivity; // Pastikan import ini sesuai dengan lokasi LoginActivity
import com.kalisat.edulearn.R;

public class SettingFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout untuk fragment ini
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Cari LinearLayout dengan ID dan tambahkan OnClickListener
        LinearLayout linearLayoutLogout = view.findViewById(R.id.linearLayout_logout); // Pastikan ID sesuai dengan layout XML
        linearLayoutLogout.setOnClickListener(v -> {
            // Tampilkan dialog konfirmasi sebelum logout
            showLogoutConfirmationDialog();
        });

        return view;
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Proses logout di sini (misalnya, menghapus shared preferences)
                    logoutUser();

                    // Pindah ke LoginActivity
                    Intent intent = new Intent(requireActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Tutup dialog jika pengguna membatalkan
                    dialog.dismiss();
                })
                .setCancelable(false); // Mencegah dialog ditutup kecuali melalui tombol

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logoutUser() {
        // Hapus status login dari SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Hapus semua data di user_session
        editor.apply();
    }
}
