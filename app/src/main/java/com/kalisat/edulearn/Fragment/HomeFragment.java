package com.kalisat.edulearn.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kalisat.edulearn.Activity.DetailJadwalActivity;
import com.kalisat.edulearn.Activity.IntroActivity;
import com.kalisat.edulearn.Activity.MainActivity;
import com.kalisat.edulearn.Activity.PanduanActivity;
import com.kalisat.edulearn.Activity.TentangActivity;
import com.kalisat.edulearn.Adapter.JadwalAdapter;
import com.kalisat.edulearn.Model.Jadwal;
import com.kalisat.edulearn.Model.ModelJadwalDetail;
import com.kalisat.edulearn.Model.ModelJadwalGrouped;
import com.kalisat.edulearn.R;
import com.kalisat.edulearn.ViewModel.JadwalViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private String userToken;
    private TextView tvWelcome;
    private RecyclerView recyclerView;
    private JadwalAdapter jadwalAdapter;
    private JadwalViewModel jadwalViewModel;
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userToken = getArguments().getString("user_token");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Inisialisasi komponen
        tvWelcome = rootView.findViewById(R.id.tv_welcome);
        recyclerView = rootView.findViewById(R.id.recyclerViewBlueBox);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        jadwalAdapter = new JadwalAdapter(new ArrayList<>());
        recyclerView.setAdapter(jadwalAdapter);

        Map<String, String> quotes = new HashMap<>();
        quotes.put("Senin", "Awali pekan ini dengan belajar penuh semangat!");
        quotes.put("Selasa", "Belajar hari ini adalah investasi masa depan.");
        quotes.put("Rabu", "Raih pemahaman baru di tengah pekan.");
        quotes.put("Kamis", "Tetap fokus, ilmu adalah kunci kesuksesan.");
        quotes.put("Jumat", "Akhiri hari dengan prestasi terbaik.");
        quotes.put("Sabtu", "Santai, namun tetap asah pengetahuan.");
        quotes.put("Minggu", "Persiapkan diri untuk belajar yang produktif.");

        // Tampilkan quote berdasarkan hari
        TextView tvQuote = rootView.findViewById(R.id.tv_quote);
        String currentDay = getCurrentDay();
        String quoteForToday = quotes.containsKey(currentDay) ? quotes.get(currentDay) : "Belajar adalah jendela dunia!";
        tvQuote.setText(quoteForToday);

        if (userToken == null) {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", requireContext().MODE_PRIVATE);
            userToken = sharedPreferences.getString("user_token", null);
        }

        jadwalViewModel = new ViewModelProvider(requireActivity()).get(JadwalViewModel.class);

        // Observasi data jadwal
        observeViewModel();

        // Panggil API jika token tersedia
        requestQueue = Volley.newRequestQueue(requireContext());
        if (userToken != null && !userToken.isEmpty()) {
            loadUserProfile();
            jadwalViewModel.fetchJadwal(requestQueue, userToken);
        }

        setupListeners(rootView);

        return rootView;
    }

    private void observeViewModel() {
        jadwalViewModel.getJadwalGroupedList().observe(getViewLifecycleOwner(), groupedList -> {
            String currentDay = getCurrentDay();
            Log.d("DEBUG", "Current Day: " + currentDay); // Log hari saat ini
            List<Jadwal> todaySchedules = new ArrayList<>();

            boolean isDataAvailable = false; // Menandai apakah data untuk hari saat ini ditemukan

            for (ModelJadwalGrouped group : groupedList) {
                if (group.getHari().equals(currentDay)) {
                    List<ModelJadwalDetail> mapelList = group.getMapelList();

                    String mapel1 = mapelList.size() > 0 ? mapelList.get(0).getNamaMapel() : "-";
                    String waktu1 = mapelList.size() > 0 ? mapelList.get(0).getJamMulai() + " - " + mapelList.get(0).getJamSelesai() : "-";
                    String mapel2 = mapelList.size() > 1 ? mapelList.get(1).getNamaMapel() : "-";
                    String waktu2 = mapelList.size() > 1 ? mapelList.get(1).getJamMulai() + " - " + mapelList.get(1).getJamSelesai() : "-";
                    String mapel3 = mapelList.size() > 2 ? mapelList.get(2).getNamaMapel() : "-";
                    String waktu3 = mapelList.size() > 2 ? mapelList.get(2).getJamMulai() + " - " + mapelList.get(2).getJamSelesai() : "-";

                    todaySchedules.add(new Jadwal(currentDay, mapel1, waktu1, mapel2, waktu2, mapel3, waktu3));
                    isDataAvailable = true; // Tandai bahwa data ditemukan
                    break;
                }
            }

            // Jika tidak ada data untuk hari saat ini, tampilkan "Libur"
            if (!isDataAvailable) {
                Log.d("DEBUG", "Data untuk hari " + currentDay + " tidak ditemukan. Menampilkan 'Libur'.");
                todaySchedules.add(new Jadwal(currentDay, "Libur", "00:00 - 00:00", "-", "-", "-", "-"));
            }

            jadwalAdapter.updateData(todaySchedules);
        });
    }

    private void setupListeners(View rootView) {
        // Listener untuk membuka TentangActivity
        ConstraintLayout tentangLayout = rootView.findViewById(R.id.tentang);
        tentangLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TentangActivity.class);
            startActivity(intent);
        });

        // Listener untuk membuka PanduanActivity
        ConstraintLayout panduanLayout = rootView.findViewById(R.id.panduan);
        panduanLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PanduanActivity.class);
            startActivity(intent);
        });

        // Listener lainnya (contoh untuk profile image)
        ImageView fotoProfil = rootView.findViewById(R.id.profile_image);
        fotoProfil.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).navigateToProfile();
            }
        });

        rootView.findViewById(R.id.tv_see_all).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DetailJadwalActivity.class);
            if (userToken != null) {
                intent.putExtra("user_token", userToken);
            }
            startActivity(intent);
        });
    }


    private void loadUserProfile() {
        String url = "https://wstif23.myhost.id/kelas_b/team_1/api/profile";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");
                            JSONObject data = dataArray.getJSONObject(0);
                            String nama = data.getString("nama");
                            tvWelcome.setText(nama);
                        } else {
                            // Jika token tidak valid, tampilkan AlertDialog
                            showInvalidTokenDialog();
                        }
                    } catch (JSONException e) {
                        Log.e("PROFILE_ERROR", "JSON Error: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("PROFILE_ERROR", "Volley Error: " + error.getMessage());
                    showInvalidTokenDialog();  // Tampilkan alert jika ada kesalahan
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + userToken);
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void showInvalidTokenDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Token tidak valid")  // Menambahkan judul
                .setMessage("silakan login kembali.")
                .setCancelable(false) // Tidak bisa ditutup kecuali dengan tombol OK
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Hapus sesi atau token yang disimpan
                        clearSession();
                        // Arahkan pengguna kembali ke halaman login/intro
                        navigateToIntro();
                    }
                });

        // Membuat dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Set warna tombol positif (OK) dengan warna biru dari resource
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.biru_tua));
    }


    private void clearSession() {
        // Hapus sesi yang terkait dengan token, misalnya dengan SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_token");  // Menghapus token yang tersimpan
        editor.apply();  // Simpan perubahan
    }

    private void navigateToIntro() {
        // Ganti ke aktivitas intro atau login yang sesuai
        Intent intent = new Intent(getActivity(), IntroActivity.class);  // Ganti dengan aktivitas login/intro yang sesuai
        startActivity(intent);
        getActivity().finish();  // Menutup aktivitas ini dan mengarahkan ke intro/login
    }

    private String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return "Senin";
            case Calendar.TUESDAY:
                return "Selasa";
            case Calendar.WEDNESDAY:
                return "Rabu";
            case Calendar.THURSDAY:
                return "Kamis";
            case Calendar.FRIDAY:
                return "Jumat";
            case Calendar.SATURDAY:
                return "Sabtu";
            case Calendar.SUNDAY:
                return "Minggu";
            default:
                return "";
        }
    }
}
