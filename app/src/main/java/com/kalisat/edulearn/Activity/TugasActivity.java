package com.kalisat.edulearn.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kalisat.edulearn.R;
import java.util.Calendar;

public class TugasActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private TextView tvAddTugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_tugas);

        // Menyesuaikan padding untuk menyesuaikan dengan system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referensi ke FAB (Floating Action Button)
        FloatingActionButton fabAddTask = findViewById(R.id.fabtugas);
        fabAddTask.setOnClickListener(v -> showAddTaskDialog());

        // Minta izin akses storage jika belum diberikan
        requestStoragePermission();
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_tugas, null);
        builder.setView(dialogView);

        // Referensi ke komponen UI menggunakan ID yang sesuai dari XML
        EditText etTaskDescription = dialogView.findViewById(R.id.etTaskDescription);
        EditText etTaskDeadline = dialogView.findViewById(R.id.etTaskDeadline);
        Button btnLampiran = dialogView.findViewById(R.id.btnLampiran);
        tvAddTugas = dialogView.findViewById(R.id.tvAddTugas);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnAssign = dialogView.findViewById(R.id.btnAssign);

        // Mengatur DatePicker untuk tenggat waktu
        etTaskDeadline.setOnClickListener(v -> showDatePickerDialog(etTaskDeadline));

        // Menambahkan logika untuk tombol "Lampiran"
        btnLampiran.setOnClickListener(v -> openFilePicker());

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
            dialog.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.90),
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnAssign.setOnClickListener(v -> {
            String description = etTaskDescription.getText().toString();
            String deadline = etTaskDeadline.getText().toString();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Pilih File"), PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                String fileName = getFileName(fileUri);
                tvAddTugas.setText("Lampiran: " + fileName);
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void showDatePickerDialog(EditText etTaskDeadline) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etTaskDeadline.setText(formattedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan
            } else {
                // Izin ditolak
            }
        }
    }
}
