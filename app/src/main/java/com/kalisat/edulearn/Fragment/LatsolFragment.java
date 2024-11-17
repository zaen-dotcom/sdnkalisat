package com.kalisat.edulearn.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kalisat.edulearn.R;

public class LatsolFragment extends Fragment {

    private static final String ARG_ID_MAPEL = "id_mapel";

    private int idMapel;

    public LatsolFragment() {
        // Required empty public constructor
    }

    public static LatsolFragment newInstance(int idMapel) {
        LatsolFragment fragment = new LatsolFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_MAPEL, idMapel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idMapel = getArguments().getInt(ARG_ID_MAPEL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_latsol, container, false);
    }
}
