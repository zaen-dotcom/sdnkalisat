package com.kalisat.edulearn.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kalisat.edulearn.Model.Jadwal;
import com.kalisat.edulearn.R;

import java.util.List;

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder> {

    private List<Jadwal> jadwalList; // List of Jadwal data

    public JadwalAdapter(List<Jadwal> jadwalList) {
        this.jadwalList = jadwalList;
    }

    @NonNull
    @Override
    public JadwalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jadwal, parent, false);
        return new JadwalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalViewHolder holder, int position) {
        Jadwal jadwal = jadwalList.get(position);

        // Set hari
        holder.tvHari.setText(jadwal.getHari());

        // Set mapel dan waktu
        holder.tvMapel1.setText(jadwal.getMapel1());
        holder.tvStartEnd1.setText(jadwal.getWaktu1());

        holder.tvMapel2.setText(jadwal.getMapel2());
        holder.tvStartEnd2.setText(jadwal.getWaktu2());

        holder.tvMapel3.setText(jadwal.getMapel3());
        holder.tvStartEnd3.setText(jadwal.getWaktu3());
    }

    @Override
    public int getItemCount() {
        return jadwalList.size();
    }

    public static class JadwalViewHolder extends RecyclerView.ViewHolder {
        TextView tvHari, tvMapel1, tvStartEnd1, tvMapel2, tvStartEnd2, tvMapel3, tvStartEnd3;

        public JadwalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHari = itemView.findViewById(R.id.tv_hari);
            tvMapel1 = itemView.findViewById(R.id.tv_mapel1);
            tvStartEnd1 = itemView.findViewById(R.id.tv_startend1);
            tvMapel2 = itemView.findViewById(R.id.tv_mapel2);
            tvStartEnd2 = itemView.findViewById(R.id.tv_startend2);
            tvMapel3 = itemView.findViewById(R.id.tv_mapel3);
            tvStartEnd3 = itemView.findViewById(R.id.tv_startend3);
        }
    }
}
