package com.kalisat.edulearn.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kalisat.edulearn.Model.ModelJadwalDetail;
import com.kalisat.edulearn.Model.ModelJadwalGrouped;
import com.kalisat.edulearn.R;

import java.util.List;

public class JadwalAdapterGrouped extends RecyclerView.Adapter<JadwalAdapterGrouped.ViewHolder> {

    private Context context;
    private List<ModelJadwalGrouped> jadwalGroupedList;

    public JadwalAdapterGrouped(Context context, List<ModelJadwalGrouped> jadwalGroupedList) {
        this.context = context;
        this.jadwalGroupedList = jadwalGroupedList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jadwal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelJadwalGrouped group = jadwalGroupedList.get(position);

        // Set nama hari
        holder.tvHari.setText(group.getHari());

        // Ambil list mapel dari grup hari
        List<ModelJadwalDetail> mapelList = group.getMapelList();

        // Pastikan semua mapel tampil
        if (mapelList.size() > 0) {
            holder.tvMapel1.setText(mapelList.get(0).getNamaMapel());
            holder.tvStartEnd1.setText(mapelList.get(0).getJamMulai() + " - " + mapelList.get(0).getJamSelesai());
        } else {
            holder.tvMapel1.setText("-");
            holder.tvStartEnd1.setText("-");
        }

        if (mapelList.size() > 1) {
            holder.tvMapel2.setText(mapelList.get(1).getNamaMapel());
            holder.tvStartEnd2.setText(mapelList.get(1).getJamMulai() + " - " + mapelList.get(1).getJamSelesai());
        } else {
            holder.tvMapel2.setText("-");
            holder.tvStartEnd2.setText("-");
        }

        if (mapelList.size() > 2) {
            holder.tvMapel3.setText(mapelList.get(2).getNamaMapel());
            holder.tvStartEnd3.setText(mapelList.get(2).getJamMulai() + " - " + mapelList.get(2).getJamSelesai());
        } else {
            holder.tvMapel3.setText("-");
            holder.tvStartEnd3.setText("-");
        }

        // Atur warna background CardView
        int colorIndex = position % 8; // Pilih warna secara berulang
        int[] colors = {
                R.color.soft_coral,
                R.color.biru_muda,
                R.color.soft_light_green,
                R.color.soft_bright_yellow,
                R.color.soft_purple,
                R.color.soft_cyan,
                R.color.soft_orange,
                R.color.soft_green
        };
        int backgroundColor = context.getResources().getColor(colors[colorIndex]);
        holder.cardView.setCardBackgroundColor(backgroundColor);
    }

    @Override
    public int getItemCount() {
        return jadwalGroupedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHari, tvMapel1, tvStartEnd1, tvMapel2, tvStartEnd2, tvMapel3, tvStartEnd3;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHari = itemView.findViewById(R.id.tv_hari);
            tvMapel1 = itemView.findViewById(R.id.tv_mapel1);
            tvStartEnd1 = itemView.findViewById(R.id.tv_startend1);
            tvMapel2 = itemView.findViewById(R.id.tv_mapel2);
            tvStartEnd2 = itemView.findViewById(R.id.tv_startend2);
            tvMapel3 = itemView.findViewById(R.id.tv_mapel3);
            tvStartEnd3 = itemView.findViewById(R.id.tv_startend3);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
