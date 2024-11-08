package com.kalisat.edulearn.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kalisat.edulearn.R;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvKelas, tvInfo, tvLabelKelas, tvLabelInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inisialisasi komponen UI
        tvName = view.findViewById(R.id.tv_name);
        tvKelas = view.findViewById(R.id.tv_kelas);
        tvInfo = view.findViewById(R.id.tv_info);
        tvLabelKelas = view.findViewById(R.id.tv_label_kelas); // Untuk label kelas/wali kelas
        tvLabelInfo = view.findViewById(R.id.tv_label_info);   // Untuk label NISN/NIPD
        LinearLayout edtPhone = view.findViewById(R.id.edtphone);
        ImageView editPhoneIcon = view.findViewById(R.id.edit_phone);
        ImageView pilihFoto = view.findViewById(R.id.pilihfoto);

        // Mengambil data dari SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_session", requireContext().MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "siswa");
        String nama = sharedPreferences.getString("nama", "Nama Tidak Diketahui");
        String kelasOrMapel = sharedPreferences.getString("kelas_or_mapel", "Informasi Tidak Diketahui");
        String nisnOrNipd = sharedPreferences.getString("nisn_or_nipd", "Informasi Tidak Diketahui");

        // Menentukan tampilan berdasarkan role
        if (role.equalsIgnoreCase("guru")) {
            // Untuk guru
            tvLabelKelas.setText("Wali Kelas/Pengajar");
            tvLabelInfo.setText("NIPD");
            tvKelas.setText(kelasOrMapel);
            tvInfo.setText(nisnOrNipd);
        } else {
            // Untuk siswa
            tvLabelKelas.setText("Kelas");
            tvLabelInfo.setText("NISN");
            tvKelas.setText(kelasOrMapel);
            tvInfo.setText(nisnOrNipd);
        }

        // Menampilkan nama
        tvName.setText(nama);

        // Listener untuk edit nomor telepon
        edtPhone.setOnClickListener(v -> showEditPhoneDialog());
        editPhoneIcon.setOnClickListener(v -> showEditPhoneDialog());

        // Listener untuk pilih foto
        pilihFoto.setOnClickListener(v -> openGallery());

        return view;
    }

    // Method untuk menampilkan dialog edit telepon
    private void showEditPhoneDialog() {
        // Implementasi dialog
    }

    // Method untuk membuka galeri
    private void openGallery() {
        // Implementasi membuka galeri
    }
}
