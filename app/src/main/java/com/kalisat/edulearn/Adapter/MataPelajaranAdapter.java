package com.kalisat.edulearn.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kalisat.edulearn.Activity.DetailMapelActivity;
import com.kalisat.edulearn.Model.MataPelajaran;
import com.kalisat.edulearn.R;

import java.util.List;

public class MataPelajaranAdapter extends RecyclerView.Adapter<MataPelajaranAdapter.MataPelajaranViewHolder> {

    private List<MataPelajaran> mataPelajaranList;
    private Context context;

    // Array warna untuk background, looping setelah 10 item
    private final int[] colors = {
            R.color.soft_coral,
            R.color.soft_sky_blue,
            R.color.soft_light_green,
            R.color.soft_bright_yellow,
            R.color.soft_purple,
            R.color.soft_cyan,
            R.color.soft_orange,
            R.color.soft_green
    };

    // Constructor
    public MataPelajaranAdapter(Context context, List<MataPelajaran> mataPelajaranList) {
        this.context = context;
        this.mataPelajaranList = mataPelajaranList;
    }

    @NonNull
    @Override
    public MataPelajaranViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kelas, parent, false);
        return new MataPelajaranViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MataPelajaranViewHolder holder, int position) {
        MataPelajaran mataPelajaran = mataPelajaranList.get(position);

        // Atur data ke view
        holder.tvJudulMapel.setText(mataPelajaran.getNamaMapel());
        holder.tvNamaGuru.setText(mataPelajaran.getNamaGuru());

        // Tentukan warna background berdasarkan posisi (loop kembali ke awal setelah 10 item)
        int colorIndex = position % colors.length; // Ambil indeks warna berdasarkan posisi
        int backgroundColor = context.getResources().getColor(colors[colorIndex]);

        // Terapkan warna ke background `ConstraintLayout`
        holder.bgWarna.setBackgroundColor(backgroundColor);

        // Klik pada item untuk membuka DetailMapelActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailMapelActivity.class);
            intent.putExtra("id_mapel", mataPelajaran.getId());
            intent.putExtra("nama_mapel", mataPelajaran.getNamaMapel());
            intent.putExtra("nama_guru", mataPelajaran.getNamaGuru());
            context.startActivity(intent);
        });

        // Klik pada tombol "Masuk Kelas" untuk membuka DetailMapelActivity
        holder.btnMasukKelas.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailMapelActivity.class);
            intent.putExtra("id_mapel", mataPelajaran.getId());
            intent.putExtra("nama_mapel", mataPelajaran.getNamaMapel());
            intent.putExtra("nama_guru", mataPelajaran.getNamaGuru());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mataPelajaranList.size();
    }

    public static class MataPelajaranViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudulMapel, tvNamaGuru;
        View bgWarna;
        Button btnMasukKelas; // Tambahkan referensi ke tombol

        public MataPelajaranViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudulMapel = itemView.findViewById(R.id.tv_judul_mapel);
            tvNamaGuru = itemView.findViewById(R.id.tv_namaguru);
            bgWarna = itemView.findViewById(R.id.bg_warna);
            btnMasukKelas = itemView.findViewById(R.id.btn_masukkelas); // Inisialisasi tombol
        }
    }
}
