package com.kalisat.edulearn.Activity;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kalisat.edulearn.R;

public class PanduanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_panduan);

        // Handle window insets for Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up back button
        ImageView backButton = findViewById(R.id.ic_kembali);
        backButton.setOnClickListener(v -> finish());

        // Apply justification for multiple TextViews
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Apply justification to TextViews
            setJustification(R.id.tugas_desc);
            setJustification(R.id.latihan_soal_desc);
            setJustification(R.id.jadwal_desc);
            setJustification(R.id.profil_desc);
            setJustification(R.id.ubah_kata_sandi_desc);
        }
    }

    // Function to apply justification mode to TextViews
    private void setJustification(int textViewId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TextView textView = findViewById(textViewId);
            textView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
    }
}
