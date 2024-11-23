package com.kalisat.edulearn.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kalisat.edulearn.Adapter.LatihanSoalAdapter;
import com.kalisat.edulearn.Model.ModelLatihanSoal;
import com.kalisat.edulearn.R;
import com.kalisat.edulearn.ViewModel.LatihanSoalViewModel;

import java.util.ArrayList;

public class LatsolFragment extends Fragment {

    private static final String ARG_ID_MAPEL = "id_mapel";
    private int idMapel;
    private RecyclerView recyclerView;
    private LatihanSoalAdapter adapter;
    private LatihanSoalViewModel latihanSoalViewModel;

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
        View view = inflater.inflate(R.layout.fragment_latsol, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewLatihanSoal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LatihanSoalAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Inisialisasi ViewModel
        latihanSoalViewModel = new ViewModelProvider(this).get(LatihanSoalViewModel.class);

        // Observasi data dari ViewModel
        latihanSoalViewModel.getLatihanSoalListLiveData().observe(getViewLifecycleOwner(), latihanSoalList -> {
            if (latihanSoalList != null && !latihanSoalList.isEmpty()) {
                adapter.setLatihanSoalList(latihanSoalList); // Perbarui data di adapter
            }
        });

        latihanSoalViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            // Tampilkan indikator loading jika perlu
        });

        latihanSoalViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            // Tampilkan pesan error jika ada
        });

        // Memanggil ViewModel untuk mengambil data
        latihanSoalViewModel.fetchLatihanSoal(idMapel);

        return view;
    }
}
