package com.kalisat.edulearn.Activity;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BulletSpan;
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

        // Apply Edge-to-Edge insets for main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up back button to return to FragmentSetting
        ImageView backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(v -> finish());

        // Justify text for description_about TextView if API level supports it
        TextView descriptionTextView = findViewById(R.id.description_about);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            descriptionTextView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        // Set bullet points with justification for feature list
        TextView featureListTextView = findViewById(R.id.feature_list);
        String featureText = "Kursus - Akses berbagai materi pembelajaran dalam bentuk teks, foto, atau dokumen.\n" +
                "Kuis - Uji pemahaman Anda dengan kuis interaktif dan kisi-kisi.\n" +
                "Tugas - Terima dan kumpulkan tugas langsung dari aplikasi.\n" +
                "Nilai - Lihat hasil belajar Anda dengan mudah dalam bentuk rapor digital.";

        SpannableString spannable = new SpannableString(featureText);

        // Set BulletSpan for each feature
        int start = 0;
        for (String line : featureText.split("\n")) {
            spannable.setSpan(new BulletSpan(30), start, start + line.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start += line.length() + 1;
        }

        featureListTextView.setText(spannable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            featureListTextView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
    }
}
