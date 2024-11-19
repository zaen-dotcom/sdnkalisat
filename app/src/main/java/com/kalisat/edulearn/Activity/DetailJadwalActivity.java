package com.kalisat.edulearn.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Adapter.JadwalAdapterGrouped;
import com.kalisat.edulearn.Model.ModelJadwalDetail;
import com.kalisat.edulearn.Model.ModelJadwalGrouped;
import com.kalisat.edulearn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailJadwalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JadwalAdapterGrouped jadwalAdapter;
    private List<ModelJadwalGrouped> jadwalGroupedList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jadwal);

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi list jadwal
        jadwalGroupedList = new ArrayList<>();

        // Inisialisasi request queue
        requestQueue = Volley.newRequestQueue(this);

        // Tangani klik tombol kembali
        findViewById(R.id.ic_kembali).setOnClickListener(v -> {
            // Tutup activity saat tombol kembali diklik
            finish();
        });

        // Ambil token dari SharedPreferences
        String token = getSharedPreferences("user_session", MODE_PRIVATE).getString("user_token", "");

        if (token.isEmpty()) {
            // Tampilkan pesan error jika token tidak tersedia
            return;
        }

        // Panggil API untuk mendapatkan data jadwal
        getDataFromAPI(token);
    }

    private void getDataFromAPI(String token) {
        String url = "http://192.168.159.228:8000/api/jadwal"; // Ganti dengan URL API Anda

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success") && response.has("data")) {
                                JSONArray dataArray = response.getJSONArray("data");

                                // Kosongkan data sebelumnya
                                jadwalGroupedList.clear();

                                // Kelompokkan data berdasarkan hari
                                Map<String, List<ModelJadwalDetail>> groupedData = new HashMap<>();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject obj = dataArray.getJSONObject(i);

                                    // Ambil atribut dari JSON
                                    String hari = obj.getString("hari");
                                    String nama = obj.getString("nama");
                                    String jamMulai = obj.getString("jam_mulai");
                                    String jamSelesai = obj.getString("jam_selesai");

                                    // Buat objek ModelJadwalDetail
                                    ModelJadwalDetail detail = new ModelJadwalDetail(nama, jamMulai, jamSelesai);

                                    // Tambahkan ke grup sesuai hari
                                    if (!groupedData.containsKey(hari)) {
                                        groupedData.put(hari, new ArrayList<>());
                                    }
                                    groupedData.get(hari).add(detail);
                                }

                                // Konversi Map menjadi List<ModelJadwalGrouped>
                                for (Map.Entry<String, List<ModelJadwalDetail>> entry : groupedData.entrySet()) {
                                    jadwalGroupedList.add(new ModelJadwalGrouped(entry.getKey(), entry.getValue()));
                                }

                                // Debugging: Periksa hasil pengelompokan
                                for (ModelJadwalGrouped group : jadwalGroupedList) {
                                    Log.d("GroupedData", "Hari: " + group.getHari() + ", Jumlah Mapel: " + group.getMapelList().size());
                                }

                                // Set adapter ke RecyclerView
                                if (recyclerView.getAdapter() == null) {
                                    jadwalAdapter = new JadwalAdapterGrouped(jadwalGroupedList);
                                    recyclerView.setAdapter(jadwalAdapter);
                                } else {
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                }
                            } else {
                                Log.e("API_Response", "Respons tidak valid.");
                            }
                        } catch (JSONException e) {
                            Log.e("JSON_ERROR", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API_ERROR", "Error: " + error.getMessage());
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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, // Timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Jumlah retry maksimal
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(jsonObjectRequest);
    }
}
