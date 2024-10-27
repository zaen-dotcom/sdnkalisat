package com.kalisat.edulearn.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kalisat.edulearn.R;

public class KursusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kursus);

        // Mengatur padding untuk sistem bar (status bar dan navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup Floating Action Button untuk menampilkan pilihan "Buat Kursus" dan "Gabung Kursus"
        FloatingActionButton fab = findViewById(R.id.fabMain);
        fab.setOnClickListener(v -> showBottomSheet());
    }

    private void showBottomSheet() {
        // Buat BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(KursusActivity.this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);

        // Mengatur event klik untuk "Buat Kursus"
        bottomSheetView.findViewById(R.id.btnBuatKursus).setOnClickListener(v -> {
            // Tambahkan aksi saat tombol "Buat Kursus" diklik
            // Misalnya, pindah ke BuatKursusActivity
            // Intent intent = new Intent(KursusActivity.this, BuatKursusActivity.class);
            // startActivity(intent);
            bottomSheetDialog.dismiss();
        });

        // Mengatur event klik untuk "Gabung Kursus"
        bottomSheetView.findViewById(R.id.btnGabungKursus).setOnClickListener(v -> {
            // Menampilkan form input untuk memasukkan kode kursus
            showJoinCourseDialog();
            bottomSheetDialog.dismiss();
        });

        // Menampilkan BottomSheetDialog
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void showJoinCourseDialog() {
        // Membuat dialog kustom dengan sudut membulat
        Dialog dialog = new Dialog(KursusActivity.this);
        dialog.setContentView(R.layout.dialog_input_kode);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background); // Menggunakan latar belakang dengan sudut membulat

        // Pastikan dialog menggunakan ukuran lebar tertentu agar tampil lebih baik
        dialog.getWindow().setLayout(
                (int) (getResources().getDisplayMetrics().widthPixels * 0.90), // 85% dari lebar layar
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Nonaktifkan opsi untuk menutup dialog dengan mengklik di luar
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        // Referensi ke input dan tombol
        EditText inputKode = dialog.findViewById(R.id.etKodeKursus);
        TextView btnGabung = dialog.findViewById(R.id.btnGabung);
        TextView btnBatal = dialog.findViewById(R.id.btnBatal);

        // Aksi tombol "Gabung"
        btnGabung.setOnClickListener(v -> {
            String kode = inputKode.getText().toString().trim();
            if (!kode.isEmpty()) {
                // Lakukan aksi dengan kode yang dimasukkan, misalnya verifikasi kode
                Toast.makeText(KursusActivity.this, "Kode yang dimasukkan: " + kode, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                // Tampilkan alert jika kode kosong
                showEmptyCodeAlert();
            }
        });

        // Aksi tombol "Batal"
        btnBatal.setOnClickListener(v -> dialog.dismiss());

        // Tampilkan dialog
        dialog.show();
    }

    private void showEmptyCodeAlert() {
        // Buat AlertDialog jika kode kosong
        AlertDialog alertDialog = new AlertDialog.Builder(KursusActivity.this)
                .setTitle("Peringatan")
                .setMessage("Kode kursus tidak boleh kosong!")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create();

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.biru_tua));
    }
}
