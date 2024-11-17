package com.kalisat.edulearn.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kalisat.edulearn.Activity.DetailTugasActivity;
import com.kalisat.edulearn.Model.ModelTugas;
import com.kalisat.edulearn.R;

import java.util.List;

public class TugasAdapter extends RecyclerView.Adapter<TugasAdapter.TugasViewHolder> {

    private Context context;
    private List<ModelTugas> tugasList;
    private int idMapel; // Tambahkan idMapel

    // Modifikasi konstruktor untuk menerima idMapel
    public TugasAdapter(Context context, List<ModelTugas> tugasList, int idMapel) {
        this.context = context;
        this.tugasList = tugasList;
        this.idMapel = idMapel; // Simpan idMapel
    }

    @NonNull
    @Override
    public TugasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tugas, parent, false);
        return new TugasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TugasViewHolder holder, int position) {
        ModelTugas tugas = tugasList.get(position);

        // Set data ke view
        holder.tvJudulTugas.setText(tugas.getJudulTugas());
        holder.tvDeadline.setText("Deadline: " + tugas.getDeadline());

        // Set onClickListener untuk berpindah ke DetailTugasActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailTugasActivity.class);

            // Kirim data tugas melalui Intent
            intent.putExtra("id", tugas.getId()); // Kirimkan ID tugas
            intent.putExtra("id_mapel", idMapel); // Kirimkan ID mapel
            intent.putExtra("judul_tugas", tugas.getJudulTugas());
            intent.putExtra("deskripsi", tugas.getDeskripsi());
            intent.putExtra("tanggal_tugas", tugas.getTanggalTugas());
            intent.putExtra("deadline", tugas.getDeadline());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tugasList.size();
    }

    public static class TugasViewHolder extends RecyclerView.ViewHolder {

        TextView tvJudulTugas, tvDeadline;

        public TugasViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudulTugas = itemView.findViewById(R.id.tv_judultugas);
            tvDeadline = itemView.findViewById(R.id.tv_deadline);
        }
    }
}
