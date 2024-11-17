package com.kalisat.edulearn.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Activity.DetailJadwalActivity;
import com.kalisat.edulearn.Activity.MainActivity;
import com.kalisat.edulearn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private String userToken;
    private TextView tvWelcome;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ambil token dari argument
        if (getArguments() != null) {
            userToken = getArguments().getString("user_token");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Inisialisasi TextView tv_welcome
        tvWelcome = rootView.findViewById(R.id.tv_welcome);

        // Ambil token dari SharedPreferences jika belum ada di argument
        if (userToken == null) {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", requireContext().MODE_PRIVATE);
            userToken = sharedPreferences.getString("user_token", null);
        }

        // Panggil metode untuk memuat data profil
        if (userToken != null && !userToken.isEmpty()) {
            loadUserProfile();
        }

        setupListeners(rootView);

        return rootView;
    }

    private void setupListeners(View rootView) {
        // Listener untuk foto profil
        ImageView fotoProfil = rootView.findViewById(R.id.profile_image);
        fotoProfil.setOnClickListener(v -> {
            // Beri callback ke MainActivity jika perlu
            if (getActivity() != null) {
                ((MainActivity) getActivity()).navigateToProfile();
            }
        });

        // Listener untuk tv_see_all
        rootView.findViewById(R.id.tv_see_all).setOnClickListener(v -> {
            // Buka DetailJadwalActivity
            Intent intent = new Intent(getActivity(), DetailJadwalActivity.class);
            if (userToken != null) {
                intent.putExtra("user_token", userToken);
            }
            startActivity(intent);
        });
    }

    private void loadUserProfile() {
        String url = "http://192.168.159.228:8000/api/profile";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String status = response.getString("status");
                        if ("success".equals(status)) {
                            JSONArray dataArray = response.getJSONArray("data");
                            JSONObject data = dataArray.getJSONObject(0);

                            String nama = data.getString("nama");

                            // Setel teks nama pengguna pada tv_welcome
                            tvWelcome.setText(nama);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + userToken);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}
