package com.kalisat.edulearn.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kalisat.edulearn.Model.ModelSoal;
import com.kalisat.edulearn.R;

import java.util.List;

public class LatihanSoalPagerAdapter extends RecyclerView.Adapter<LatihanSoalPagerAdapter.ViewHolder> {

    private List<ModelSoal> soalList;

    public LatihanSoalPagerAdapter(List<ModelSoal> soalList) {
        this.soalList = soalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_latihansoal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelSoal soal = soalList.get(position);

        // Set data ke TextView sesuai data soal
        holder.tvSoal.setText(soal.getSoal());
        holder.tvAnswer1.setText(soal.getPilihanA());
        holder.tvAnswer2.setText(soal.getPilihanB());
        holder.tvAnswer3.setText(soal.getPilihanC());
        holder.tvAnswer4.setText(soal.getPilihanD());

        // Tambahkan click listener untuk setiap jawaban (opsional)
        holder.tvAnswer1.setOnClickListener(v -> {
            // Tindakan ketika jawaban 1 diklik
        });
        holder.tvAnswer2.setOnClickListener(v -> {
            // Tindakan ketika jawaban 2 diklik
        });
        holder.tvAnswer3.setOnClickListener(v -> {
            // Tindakan ketika jawaban 3 diklik
        });
        holder.tvAnswer4.setOnClickListener(v -> {
            // Tindakan ketika jawaban 4 diklik
        });
    }

    @Override
    public int getItemCount() {
        return soalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSoal, tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSoal = itemView.findViewById(R.id.tv_soal);
            tvAnswer1 = itemView.findViewById(R.id.tv_answer1);
            tvAnswer2 = itemView.findViewById(R.id.tv_answer2);
            tvAnswer3 = itemView.findViewById(R.id.tv_answer3);
            tvAnswer4 = itemView.findViewById(R.id.tv_answer4);
        }
    }
}
