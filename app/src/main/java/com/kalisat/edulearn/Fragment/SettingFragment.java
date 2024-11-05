package com.kalisat.edulearn.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import com.kalisat.edulearn.Activity.LoginActivity;
import com.kalisat.edulearn.Activity.ThemeActivity;
import com.kalisat.edulearn.Activity.TentangActivity;
import com.kalisat.edulearn.Activity.UbahsandiActivity;
import com.kalisat.edulearn.R;

public class SettingFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout untuk fragment ini
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Cari LinearLayout dengan ID dan tambahkan OnClickListener untuk logout
        LinearLayout linearLayoutLogout = view.findViewById(R.id.linearLayout_logout); // Pastikan ID sesuai dengan layout XML
        linearLayoutLogout.setOnClickListener(v -> {
            // Tampilkan dialog konfirmasi sebelum logout
            showLogoutConfirmationDialog();
        });

        // Cari LinearLayout lainnya dan tambahkan OnClickListener untuk membuka Activity
        LinearLayout tema = view.findViewById(R.id.tema);
        LinearLayout tentang = view.findViewById(R.id.tentang);
        LinearLayout ubahSandi = view.findViewById(R.id.ubahsandi); // Pastikan ID ini sesuai dengan layout XML Anda

        tema.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ThemeActivity.class);
            startActivity(intent);
        });

        tentang.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), TentangActivity.class);
            startActivity(intent);
        });

        ubahSandi.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), UbahsandiActivity.class);
            startActivity(intent);
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
