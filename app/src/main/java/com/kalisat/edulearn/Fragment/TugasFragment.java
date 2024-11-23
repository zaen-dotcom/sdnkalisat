package com.kalisat.edulearn.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Adapter.TugasAdapter;
import com.kalisat.edulearn.Model.ModelTugas;
import com.kalisat.edulearn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TugasFragment extends Fragment {

    private static final String ARG_ID_MAPEL = "id_mapel";
    private static final String ARG_NAMA_MAPEL = "nama_mapel";

    private RecyclerView recyclerView;
    private TugasAdapter adapter;
    private List<ModelTugas> tugasList;
    private RequestQueue requestQueue;
    private TextView tvNoData, tvMapelTitle;
    private ImageView icKembali; // Tambahkan variabel untuk ikon kembali
    private int idMapel;
    private String namaMapel;

    public TugasFragment() {
        // Required empty public constructor
    }

    // Factory method untuk membuat instance fragment dengan parameter
    public static TugasFragment newInstance(int idMapel, String namaMapel) {
        TugasFragment fragment = new TugasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_MAPEL, idMapel);
        args.putString(ARG_NAMA_MAPEL, namaMapel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idMapel = getArguments().getInt(ARG_ID_MAPEL);
            namaMapel = getArguments().getString(ARG_NAMA_MAPEL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tugas, container, false);

        // Inisialisasi TextView dan set nilai namaMapel
        tvMapelTitle = view.findViewById(R.id.tv_mapel_title);
        tvMapelTitle.setText(namaMapel); // Set nama mapel ke TextView

        // Inisialisasi RecyclerView dan TextView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tvNoData = view.findViewById(R.id.tv_no_data);

        // Inisialisasi ImageView untuk ikon kembali
        icKembali = view.findViewById(R.id.ic_kembali);
        icKembali.setOnClickListener(v -> {
            // Tindakan saat ikon kembali diklik
            if (getActivity() != null) {
                getActivity().onBackPressed(); // Kembali ke fragment sebelumnya
            } else {
                Toast.makeText(getContext(), "Tidak dapat kembali", Toast.LENGTH_SHORT).show();
            }
        });

        // Inisialisasi list data dan adapter
        tugasList = new ArrayList<>();
        adapter = new TugasAdapter(getContext(), tugasList, idMapel);
        recyclerView.setAdapter(adapter);

        // Inisialisasi RequestQueue untuk Volley
        requestQueue = Volley.newRequestQueue(getContext());

        // Panggil method untuk mengambil data dari API
        getDataFromAPI();

        return view;
    }

    private void getDataFromAPI() {
        // Gunakan idMapel untuk membentuk URL
        String url = "http://192.168.159.228:8000/api/mapel-kelas/" + idMapel + "/tugas";

        // Ambil token dari SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", "");

        // Jika token tidak tersedia, tampilkan pesan dan hentikan proses
        if (token.isEmpty()) {
            Toast.makeText(getContext(), "Token tidak tersedia. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("TugasFragment", "URL API: " + url);
        Log.d("TugasFragment", "Token: " + token);

        // Buat request ke API menggunakan Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Memeriksa apakah respons berhasil
                            if (response.getString("status").equals("success")) {
                                tugasList.clear();
                                JSONArray dataArray = response.getJSONArray("data");

                                // Iterasi array data
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObj = dataArray.getJSONObject(i);

                                    // Ambil data dari JSON
                                    int id = dataObj.getInt("id"); // Ambil ID
                                    String judulTugas = dataObj.optString("judul_tugas", "Tugas Tidak Diketahui");
                                    String deskripsi = dataObj.optString("deskripsi", "Deskripsi tidak tersedia"); // Ambil deskripsi
                                    String tanggalTugas = dataObj.optString("tanggal_tugas", "Tidak ada tanggal tugas");
                                    String deadline = dataObj.optString("deadline", "Tidak ada deadline");

                                    Log.d("TugasFragment", "ID: " + id + ", Judul: " + judulTugas);

                                    // Tambahkan tugas ke daftar
                                    tugasList.add(new ModelTugas(id, judulTugas, deskripsi, tanggalTugas, deadline));
                                }

                                // Periksa apakah daftar tugas kosong
                                if (tugasList.isEmpty()) {
                                    tvNoData.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                } else {
                                    tvNoData.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }

                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(getContext(), "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Kesalahan parsing data.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Kesalahan koneksi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        // Tambahkan request ke RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}
