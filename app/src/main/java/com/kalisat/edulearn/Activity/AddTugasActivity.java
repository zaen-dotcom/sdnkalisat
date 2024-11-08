package com.kalisat.edulearn.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kalisat.edulearn.R;

import java.util.Calendar;

public class AddTugasActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private TextView tvAddTugas;
    private TextView tvDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtugas);

        tvAddTugas = findViewById(R.id.textViewAttachment);
        tvDueDate = findViewById(R.id.textViewDueDate); // Referensi ke TextView "Atur Tenggat Waktu"
        LinearLayout attachmentSection = findViewById(R.id.attachmentSection);
        Switch switchDueDate = findViewById(R.id.switchDueDate);
        ImageView backIcon = findViewById(R.id.backIcon); // Referensi backIcon

        // Event saat Switch diaktifkan
        switchDueDate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showDatePickerDialog(tvDueDate, switchDueDate); // Perbarui tvDueDate
            }
        });

        // Event saat attachmentSection diklik
        attachmentSection.setOnClickListener(v -> openFilePicker());

        // Event saat backIcon diklik
        backIcon.setOnClickListener(v -> {
            onBackPressed(); // Kembali ke aktivitas sebelumnya
        });
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

    private void showDatePickerDialog(TextView textView, Switch switchDueDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Buat DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = "Tenggat: " + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textView.setText(formattedDate);
                },
                year, month, day);

        // Event jika user menekan "Cancel"
        datePickerDialog.setOnCancelListener(dialog -> {
            // Matikan switch jika user membatalkan
            switchDueDate.setChecked(false);
        });

        // Tampilkan dialog
        datePickerDialog.show();
    }
}
