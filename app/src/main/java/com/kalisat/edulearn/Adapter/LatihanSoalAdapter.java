package com.kalisat.edulearn.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kalisat.edulearn.Activity.DetailLatihanSoalActivity;
import com.kalisat.edulearn.Model.ModelLatihanSoal;
import com.kalisat.edulearn.R;

import java.util.List;

public class LatihanSoalAdapter extends RecyclerView.Adapter<LatihanSoalAdapter.ViewHolder> {

    private Context context;
    private List<ModelLatihanSoal> latihanSoalList;

    // Array warna untuk background, looping setelah habis
    private final int[] colors = {
            R.color.soft_coral,
            R.color.soft_sky_blue,
            R.color.soft_light_green,
            R.color.soft_bright_yellow,
            R.color.soft_purple,
            R.color.soft_cyan,
            R.color.soft_red,
            R.color.soft_green
    };

    public LatihanSoalAdapter(Context context, List<ModelLatihanSoal> latihanSoalList) {
        this.context = context;
        this.latihanSoalList = latihanSoalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_soal, parent, false);
        return new ViewHolder(view);
    }

    public void setLatihanSoalList(List<ModelLatihanSoal> latihanSoalList) {
        this.latihanSoalList = latihanSoalList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelLatihanSoal latihanSoal = latihanSoalList.get(position);

        // Atur data ke TextView
        holder.tvJudul.setText(latihanSoal.getJudulSoal());
        holder.tvStart.setText("Start: " + latihanSoal.getTanggalSoal());
        holder.tvEnd.setText("End: " + latihanSoal.getDeadline());

        // Tentukan warna background berdasarkan posisi (loop kembali ke awal setelah habis)
        int colorIndex = position % colors.length;
        int backgroundColor = context.getResources().getColor(colors[colorIndex]);

        // Terapkan warna ke latar belakang ConstraintLayout
        holder.bgConstraint.setBackgroundColor(backgroundColor);

        // Tambahkan klik listener untuk membuka DetailLatihanSoalActivity
        holder.itemView.setOnClickListener(v -> {
            // Intent untuk membuka DetailLatihanSoalActivity
            Intent intent = new Intent(context, DetailLatihanSoalActivity.class);

            // Kirim data ke Activity menggunakan intent
            intent.putExtra("id_soal", latihanSoal.getId()); // Kirim ID soal
            intent.putExtra("judul_soal", latihanSoal.getJudulSoal());
            intent.putExtra("tanggal_soal", latihanSoal.getTanggalSoal());
            intent.putExtra("deadline", latihanSoal.getDeadline());

            // Mulai Activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return latihanSoalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvStart, tvEnd;
        View bgConstraint; // Referensi ke ConstraintLayout

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_judul);
            tvStart = itemView.findViewById(R.id.tv_start);
            tvEnd = itemView.findViewById(R.id.tv_end);
            bgConstraint = itemView.findViewById(R.id.bg_constraint);
        }
    }
}