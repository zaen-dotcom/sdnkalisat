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

public class TentangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tentang);

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
            // Setting justification for textViews if SDK >= O
            setJustification(R.id.textView24);
            setJustification(R.id.textView26);
            setJustification(R.id.textView22);
        }

        // Set the text for textView26
        TextView textView26 = findViewById(R.id.textView26);
        String text = "1. Tugas pembelajaran \n" +
                "2. Latihan soal dan Evaluasi \n" +
                "3. Jadwal Terstruktur \n" +
                "4. Profil Pengguna \n" +
                "5. Ubah kata sandi";
        textView26.setText(text);

    }

    private void setJustification(int textViewId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            TextView textView = findViewById(textViewId);
            textView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
    }
}
