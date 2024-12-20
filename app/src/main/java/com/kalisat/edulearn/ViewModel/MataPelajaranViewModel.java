package com.kalisat.edulearn.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Model.MataPelajaran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MataPelajaranViewModel extends AndroidViewModel {
    private final MutableLiveData<List<MataPelajaran>> mataPelajaranListLiveData = new MutableLiveData<>();
    private final RequestQueue requestQueue;

    public MataPelajaranViewModel(@NonNull Application application) {
        super(application);
        requestQueue = Volley.newRequestQueue(application.getApplicationContext());
    }

    public MutableLiveData<List<MataPelajaran>> getMataPelajaranListLiveData() {
        return mataPelajaranListLiveData;
    }

    public void fetchMataPelajaran() {
        String url = "http://192.168.218.228:8000/api/mapel-kelas";

        // Ambil token dari SharedPreferences
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", "");

        if (token.isEmpty()) {
            Toast.makeText(getApplication(), "Token tidak tersedia. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d("API_RESPONSE", response.toString());
                        if (response.has("status") && response.getString("status").equals("success") && response.has("data")) {
                            List<MataPelajaran> mataPelajaranList = new ArrayList<>();
                            JSONArray dataArray = response.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObj = dataArray.getJSONObject(i);
                                int id = dataObj.getInt("id");
                                String namaMapel = dataObj.getString("nama_mapel");
                                String namaGuru = dataObj.getString("nama_guru");
                                mataPelajaranList.add(new MataPelajaran(id, namaMapel, namaGuru));
                            }

                            // Update LiveData
                            mataPelajaranListLiveData.postValue(mataPelajaranList);
                        } else {
                            Toast.makeText(getApplication(), "Respons JSON tidak valid.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Error parsing JSON: " + e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(getApplication(), "Kesalahan parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VOLLEY_ERROR", "Error: " + error.toString());
                    if (error instanceof com.android.volley.TimeoutError) {
                        Toast.makeText(getApplication(), "Error: Timeout dari server. Coba lagi.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(jsonObjectRequest);
    }
}
