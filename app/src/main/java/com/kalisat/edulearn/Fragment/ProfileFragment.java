package com.kalisat.edulearn.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Activity.IntroActivity;
import com.kalisat.edulearn.Activity.UbahSandiActivity;
import com.kalisat.edulearn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private String userToken;

    // Deklarasi TextViews untuk profil
    private TextView tvnama, tvnisn, tvkelas, tv_nomor, tvnamawalikelas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Ambil token dari SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", requireContext().MODE_PRIVATE);
        userToken = sharedPreferences.getString("user_token", null);

        if (userToken == null || userToken.isEmpty()) {
            navigateToIntro();
            return view;
        }

        // Inisialisasi TextViews
        tvnama = view.findViewById(R.id.tvnama);
        tvnisn = view.findViewById(R.id.tvnisn);
        tvkelas = view.findViewById(R.id.tvkelas);
        tv_nomor = view.findViewById(R.id.tv_nomor);
        tvnamawalikelas = view.findViewById(R.id.namawalikelas);

        loadProfile();

        loadWaliKelas();

        CardView logoutCard = view.findViewById(R.id.card_logout);
        logoutCard.setOnClickListener(v -> showLogoutConfirmationDialog());

        // Listener untuk gantipw (CardView) dan ic_editpw (ImageView)
        CardView gantiPw = view.findViewById(R.id.gantipw);
        ImageView icEditPw = view.findViewById(R.id.ic_editpw);

        gantiPw.setOnClickListener(v -> openUbahSandiActivity());
        icEditPw.setOnClickListener(v -> openUbahSandiActivity());

        return view;
    }

    // Metode untuk memuat data profil
    private void loadProfile() {
        String url = "https://wstif23.myhost.id/kelas_b/team_1/api/profile";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d("ProfileFragment", "Response: " + response.toString());

                        String status = response.getString("status");
                        if (status.equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");
                            JSONObject data = dataArray.getJSONObject(0);

                            String nama = data.getString("nama");
                            String nisn = data.getString("nisn");
                            String kelas = data.getString("kelas");
                            String nomor_hp = data.getString("nomor_hp");

                            tvnama.setText(nama);
                            tvnisn.setText(nisn);
                            tvkelas.setText(kelas);
                            tv_nomor.setText(nomor_hp);
                        } else {
                            // Tampilkan alert jika token tidak valid
                            showInvalidTokenAlert();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showInvalidTokenAlert();
                    }
                },
                error -> {
                    error.printStackTrace();
                    showInvalidTokenAlert();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + userToken);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    private void loadWaliKelas() {
        String url = "https://wstif23.myhost.id/kelas_b/team_1/api/wali";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            JSONObject data = response.getJSONObject("data");
                            String namaWali = data.getString("nama");

                            tvnamawalikelas.setText(namaWali); // Set nama wali kelas ke TextView
                        } else {
                            tvnamawalikelas.setText("Tidak ditemukan");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        tvnamawalikelas.setText("Error memuat data");
                    }
                },
                error -> {
                    error.printStackTrace();
                    tvnamawalikelas.setText("Gagal memuat");
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + userToken);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    private void openUbahSandiActivity() {
        Intent intent = new Intent(getActivity(), UbahSandiActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                Toast.makeText(getContext(), "File yang dipilih: " + fileUri.getPath(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("OK", (dialogInterface, which) -> logout())
                .setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.biru_tua));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.biru_tua));
    }

    private void logout() {
        String url = "https://wstif23.myhost.id/kelas_b/team_1/api/logout";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    try {
                        String status = response.getString("status");
                        if ("success".equals(status)) {
                            clearSession();
                            navigateToIntro();
                        } else {
                            showInvalidTokenAlert();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showInvalidTokenAlert();
                    }
                },
                error -> {
                    showInvalidTokenAlert();
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

    private void clearSession() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_token");
        editor.apply();
    }


    private void navigateToIntro() {
        Intent intent = new Intent(getActivity(), IntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // Menampilkan alert dialog token tidak valid
    private void showInvalidTokenAlert() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Token Tidak Valid")
                .setMessage("silahkan login kembali.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    clearSession();
                    navigateToIntro();
                })
                .show();
    }

}

