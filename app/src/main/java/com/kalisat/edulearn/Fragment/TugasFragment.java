package com.kalisat.edulearn.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.kalisat.edulearn.R;

public class TugasFragment extends Fragment {

    private int idMapel;
    private String mapelName; // Nama mata pelajaran

    public TugasFragment(int idMapel, String mapelName) {
        this.idMapel = idMapel;
        this.mapelName = mapelName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tugas, container, false);

        // Menampilkan nama mata pelajaran di TextView
        TextView tvMapelTitle = view.findViewById(R.id.tv_mapel_title);
        if (mapelName != null) {
            tvMapelTitle.setText(mapelName);
        }

        // Anda dapat menambahkan logika untuk mengambil data tugas di sini

        return view;
    }
}
