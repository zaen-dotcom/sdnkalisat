package com.kalisat.edulearn.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kalisat.edulearn.Model.ModelJadwalDetail;
import com.kalisat.edulearn.Model.ModelJadwalGrouped;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

public class JadwalViewModel extends ViewModel {

    private final MutableLiveData<List<ModelJadwalGrouped>> jadwalGroupedList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // Urutan hari
    private final List<String> hariOrder = List.of("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu");

    public LiveData<List<ModelJadwalGrouped>> getJadwalGroupedList() {
        return jadwalGroupedList;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchJadwal(RequestQueue requestQueue, String token) {
        isLoading.setValue(true);

        String url = "http://192.168.218.228:8000/api/jadwal";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success") && response.has("data")) {
                            JSONArray dataArray = response.getJSONArray("data");

                            // Kosongkan data sebelumnya
                            Map<String, List<ModelJadwalDetail>> groupedData = new HashMap<>();

                            // Kelompokkan data berdasarkan hari
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);

                                String hari = obj.getString("hari");
                                String nama = obj.getString("nama");
                                String jamMulai = obj.getString("jam_mulai");
                                String jamSelesai = obj.getString("jam_selesai");

                                ModelJadwalDetail detail = new ModelJadwalDetail(nama, jamMulai, jamSelesai);

                                if (!groupedData.containsKey(hari)) {
                                    groupedData.put(hari, new ArrayList<>());
                                }
                                groupedData.get(hari).add(detail);
                            }

                            List<ModelJadwalGrouped> groupedList = new ArrayList<>();
                            for (Map.Entry<String, List<ModelJadwalDetail>> entry : groupedData.entrySet()) {
                                groupedList.add(new ModelJadwalGrouped(entry.getKey(), entry.getValue()));
                            }


                            Collections.sort(groupedList, (o1, o2) -> {
                                int index1 = hariOrder.indexOf(o1.getHari());
                                int index2 = hariOrder.indexOf(o2.getHari());
                                return Integer.compare(index1, index2);
                            });

                            jadwalGroupedList.setValue(groupedList);
                            isLoading.setValue(false);
                        } else {
                            errorMessage.setValue("Respons tidak valid dari server.");
                            isLoading.setValue(false);
                        }
                    } catch (JSONException e) {
                        errorMessage.setValue("Error parsing JSON: " + e.getMessage());
                        isLoading.setValue(false);
                    }
                },
                error -> {
                    errorMessage.setValue("Error: " + error.getMessage());
                    isLoading.setValue(false);
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