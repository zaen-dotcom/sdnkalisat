package com.kalisat.edulearn.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Model.ModelTugas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TugasViewModel extends AndroidViewModel {
    private final RequestQueue requestQueue;
    private final MutableLiveData<List<ModelTugas>> tugasListLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>();

    public TugasViewModel(@NonNull Application application) {
        super(application);
        requestQueue = Volley.newRequestQueue(application.getApplicationContext());
    }

    public MutableLiveData<List<ModelTugas>> getTugasListLiveData() {
        return tugasListLiveData;
    }

    public MutableLiveData<String> getErrorMessageLiveData() {
        return errorMessageLiveData;
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }

    public void fetchTugas(int idMapel) {
        String url = "http://192.168.218.228:8000/api/mapel-kelas/" + idMapel + "/tugas";

        // Ambil token dari SharedPreferences
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", "");

        if (token.isEmpty()) {
            errorMessageLiveData.postValue("Token tidak tersedia. Silakan login ulang.");
            return;
        }

        Log.d("TugasViewModel", "URL API: " + url);

        isLoadingLiveData.postValue(true); // Set loading state

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    isLoadingLiveData.postValue(false); // Set loading selesai
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");
                            List<ModelTugas> tugasList = new ArrayList<>();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObj = dataArray.getJSONObject(i);
                                int id = dataObj.getInt("id");
                                String judulTugas = dataObj.optString("judul_tugas", "Tugas Tidak Diketahui");
                                String deskripsi = dataObj.optString("deskripsi", "Deskripsi tidak tersedia");
                                String tanggalTugas = dataObj.optString("tanggal_tugas", "Tidak ada tanggal tugas");
                                String deadline = dataObj.optString("deadline", "Tidak ada deadline");

                                tugasList.add(new ModelTugas(id, judulTugas, deskripsi, tanggalTugas, deadline));
                            }

                            tugasListLiveData.postValue(tugasList);
                        } else {
                            errorMessageLiveData.postValue("Gagal mengambil data dari server.");
                        }
                    } catch (JSONException e) {
                        errorMessageLiveData.postValue("Kesalahan parsing data: " + e.getMessage());
                        e.printStackTrace();
                    }
                },
                error -> {
                    isLoadingLiveData.postValue(false); // Set loading selesai
                    errorMessageLiveData.postValue("Kesalahan koneksi: " + error.getMessage());
                    error.printStackTrace();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}
