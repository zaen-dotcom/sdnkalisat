package com.kalisat.edulearn.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Adapter.LatihanSoalAdapter;
import com.kalisat.edulearn.Model.ModelLatihanSoal;
import com.kalisat.edulearn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LatsolFragment extends Fragment {

    private static final String ARG_ID_MAPEL = "id_mapel";
    private int idMapel;

    private RecyclerView recyclerView;
    private LatihanSoalAdapter adapter;
    private List<ModelLatihanSoal> latihanSoalList;
    private RequestQueue requestQueue;
    private TextView tvLatsol;

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
        View view = inflater.inflate(R.layout.fragment_latsol, container, false);

        // Inisialisasi RecyclerView dan TextView
        recyclerView = view.findViewById(R.id.recyclerViewLatihanSoal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tvLatsol = view.findViewById(R.id.tv_latsol);

        // Inisialisasi list data dan adapter
        latihanSoalList = new ArrayList<>();
        adapter = new LatihanSoalAdapter(getContext(), latihanSoalList);
        recyclerView.setAdapter(adapter);

        // Inisialisasi RequestQueue untuk Volley
        requestQueue = Volley.newRequestQueue(getContext());

        // Panggil method untuk mengambil data dari API
        getLatihanSoal();

        return view;
    }

    private void getLatihanSoal() {
        String url = "http://192.168.159.228:8000/api/mapel-kelas/" + idMapel + "/latihan-soal";

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", "");

        if (token.isEmpty()) {
            Toast.makeText(getContext(), "Token tidak tersedia. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");

                            // Bersihkan list sebelum menambahkan data baru
                            latihanSoalList.clear();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);

                                // Ambil data dari JSON
                                int id = obj.getInt("id");
                                String judulSoal = obj.getString("judul_soal");
                                String tanggalSoal = obj.getString("tanggal_soal");
                                String deadline = obj.getString("deadline");

                                // Tambahkan data ke list
                                latihanSoalList.add(new ModelLatihanSoal(id, judulSoal, tanggalSoal, deadline));
                            }

                            // Beritahu adapter bahwa data telah berubah
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Gagal mendapatkan data.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Kesalahan parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(jsonObjectRequest);
    }

}
