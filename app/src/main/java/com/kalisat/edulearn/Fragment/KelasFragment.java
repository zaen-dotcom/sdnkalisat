package com.kalisat.edulearn.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Adapter.MataPelajaranAdapter;
import com.kalisat.edulearn.Model.MataPelajaran;
import com.kalisat.edulearn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KelasFragment extends Fragment {

    private RecyclerView recyclerView;
    private MataPelajaranAdapter adapter;
    private List<MataPelajaran> mataPelajaranList;
    private RequestQueue requestQueue;
    private TextView tvNoData;  // TextView untuk pesan jika tidak ada data

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelas, container, false);

        // Inisialisasi RecyclerView dan TextView
        recyclerView = view.findViewById(R.id.recycler_mapel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tvNoData = view.findViewById(R.id.tv_no_data);  // Inisialisasi TextView no data

        // Inisialisasi list data dan adapter
        mataPelajaranList = new ArrayList<>();
        adapter = new MataPelajaranAdapter(getContext(), mataPelajaranList);
        recyclerView.setAdapter(adapter);

        // Inisialisasi RequestQueue untuk Volley
        requestQueue = Volley.newRequestQueue(getContext());

        // Panggil method untuk mengambil data dari API
        getDataFromAPI();

        return view;
    }

    private void getDataFromAPI() {
        String url = "http://192.168.159.228:8000/api/mapel-kelas";

        // Ambil token dari SharedPreferences "user_session" dengan kunci "user_token"
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", "");

        // Jika token tidak tersedia, tampilkan pesan dan hentikan proses
        if (token.isEmpty()) {
            Toast.makeText(getContext(), "Token tidak tersedia. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Memeriksa apakah respons berhasil
                            if (response.getString("status").equals("success")) {
                                // Menghapus data lama sebelum menambah data baru
                                mataPelajaranList.clear();

                                // Parsing JSON array "data"
                                JSONArray dataArray = response.getJSONArray("data");

                                // Loop melalui data array dan masukkan ke dalam list
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObj = dataArray.getJSONObject(i);

                                    int id = dataObj.getInt("id");
                                    String namaMapel = dataObj.getString("nama_mapel");
                                    String namaGuru = dataObj.getString("nama_guru");

                                    // Tambahkan data ke list
                                    MataPelajaran mataPelajaran = new MataPelajaran(id, namaMapel, namaGuru);
                                    mataPelajaranList.add(mataPelajaran);
                                }

                                // Periksa apakah list kosong
                                if (mataPelajaranList.isEmpty()) {
                                    // Tampilkan pesan jika tidak ada data
                                    tvNoData.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                } else {
                                    // Sembunyikan pesan dan tampilkan RecyclerView jika ada data
                                    tvNoData.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }

                                // Notifikasi adapter bahwa data telah berubah
                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
