package com.kalisat.edulearn.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Model.ModelLatihanSoal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LatihanSoalViewModel extends AndroidViewModel {

    private final MutableLiveData<List<ModelLatihanSoal>> latihanSoalListLiveData = new MutableLiveData<>();
    private final RequestQueue requestQueue;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LatihanSoalViewModel(@NonNull Application application) {
        super(application);
        requestQueue = Volley.newRequestQueue(application.getApplicationContext());
    }

    public MutableLiveData<List<ModelLatihanSoal>> getLatihanSoalListLiveData() {
        return latihanSoalListLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchLatihanSoal(int idMapel) {
        String url = "http://192.168.159.228:8000/api/mapel-kelas/" + idMapel + "/latihan-soal";

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", "");

        if (token.isEmpty()) {
            errorMessage.postValue("Token tidak tersedia. Silakan login ulang.");
            return;
        }

        isLoading.postValue(true); // Set loading state true

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    isLoading.postValue(false); // Set loading state false
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");
                            List<ModelLatihanSoal> latihanSoalList = new ArrayList<>();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);

                                // Ambil data dari JSON
                                int id = obj.getInt("id");
                                String judulSoal = obj.getString("judul_soal");
                                String tanggalSoal = obj.getString("tanggal_soal");
                                String deadline = obj.getString("deadline");

                                latihanSoalList.add(new ModelLatihanSoal(id, judulSoal, tanggalSoal, deadline));
                            }

                            latihanSoalListLiveData.postValue(latihanSoalList);
                        } else {
                            errorMessage.postValue("Gagal mengambil data.");
                        }
                    } catch (JSONException e) {
                        errorMessage.postValue("Kesalahan parsing data: " + e.getMessage());
                        e.printStackTrace();
                    }
                },
                error -> {
                    isLoading.postValue(false); // Set loading state false
                    errorMessage.postValue("Gagal mengambil data: " + error.getMessage());
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
