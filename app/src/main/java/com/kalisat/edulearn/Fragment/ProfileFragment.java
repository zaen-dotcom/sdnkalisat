package com.kalisat.edulearn.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.kalisat.edulearn.R;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout untuk fragment ini
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inisialisasi LinearLayout edtphone
        LinearLayout edtPhone = view.findViewById(R.id.edtphone);
        edtPhone.setOnClickListener(v -> showEditPhoneDialog());

        // Inisialisasi ImageView edit_phone
        ImageView editPhoneIcon = view.findViewById(R.id.edit_phone);
        editPhoneIcon.setOnClickListener(v -> showEditPhoneDialog());

        // Inisialisasi ImageView pilihfoto
        ImageView pilihFoto = view.findViewById(R.id.pilihfoto);
        pilihFoto.setOnClickListener(v -> openGallery());

        return view;
    }

    // Method untuk menampilkan dialog_edit_phone
    private void showEditPhoneDialog() {
        // Membuat dialog kustom
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_edit_phone); // Pastikan layout ini sesuai dengan XML yang Anda berikan
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background); // Menggunakan latar belakang dengan sudut membulat

        // Mengatur ukuran dialog agar tampil 90% dari lebar layar
        dialog.getWindow().setLayout(
                (int) (getResources().getDisplayMetrics().widthPixels * 0.90), // 90% dari lebar layar
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Nonaktifkan opsi untuk menutup dialog dengan mengklik di luar
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        // Referensi ke input dan tombol
        EditText inputPhone = dialog.findViewById(R.id.etKodeKursus);
        TextView btnConfirm = dialog.findViewById(R.id.btnkonfir);
        TextView btnCancel = dialog.findViewById(R.id.btnBatal);

        // Aksi tombol "Konfirmasi"
        btnConfirm.setOnClickListener(v -> {
            String phone = inputPhone.getText().toString().trim();
            if (!phone.isEmpty()) {
                // Lakukan aksi dengan nomor HP yang dimasukkan, misalnya menyimpan data
                Toast.makeText(requireContext(), "Nomor HP disimpan: " + phone, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                // Tampilkan alert jika input kosong
                Toast.makeText(requireContext(), "Nomor HP tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });

        // Aksi tombol "Batal"
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Tampilkan dialog
        dialog.show();
    }

    // Method untuk membuka galeri
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            // Lakukan aksi dengan URI gambar, seperti menampilkan gambar di ImageView atau menyimpan gambar tersebut
            Toast.makeText(requireContext(), "Gambar terpilih: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
