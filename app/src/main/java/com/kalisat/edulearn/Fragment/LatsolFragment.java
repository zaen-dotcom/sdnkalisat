package com.kalisat.edulearn.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kalisat.edulearn.R;

public class LatsolFragment extends Fragment {

    private int idMapel;

    public LatsolFragment(int idMapel) {
        this.idMapel = idMapel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latsol, container, false);
        // Gunakan idMapel untuk mengambil data latihan soal (API bisa diimplementasikan nanti)
        return view;
    }
}
