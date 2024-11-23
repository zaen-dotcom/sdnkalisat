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

        // Set data soal dan pilihan jawaban
        holder.tvSoal.setText(soal.getSoal());
        holder.tvAnswer1.setText(soal.getPilihanA());
        holder.tvAnswer2.setText(soal.getPilihanB());
        holder.tvAnswer3.setText(soal.getPilihanC());
        holder.tvAnswer4.setText(soal.getPilihanD());

        // Update tampilan centang berdasarkan jawaban yang dipilih
        updateAnswerSelection(holder, soal);

        // Tambahkan listener untuk setiap jawaban
        holder.tvAnswer1.setOnClickListener(v -> handleAnswerSelection(holder, soal, "a"));
        holder.tvAnswer2.setOnClickListener(v -> handleAnswerSelection(holder, soal, "b"));
        holder.tvAnswer3.setOnClickListener(v -> handleAnswerSelection(holder, soal, "c"));
        holder.tvAnswer4.setOnClickListener(v -> handleAnswerSelection(holder, soal, "d"));
    }

    @Override
    public int getItemCount() {
        return soalList.size();
    }

    // Menangani pilihan jawaban
    private void handleAnswerSelection(ViewHolder holder, ModelSoal soal, String answer) {
        soal.setSelectedAnswer(answer); // Simpan jawaban yang dipilih
        updateAnswerSelection(holder, soal); // Perbarui tampilan centang
    }

    // Memperbarui tampilan centang
    private void updateAnswerSelection(ViewHolder holder, ModelSoal soal) {
        // Tampilkan centang pada jawaban yang dipilih
        holder.tvAnswer1.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                "a".equals(soal.getSelectedAnswer()) ? R.drawable.ic_check : 0, 0);
        holder.tvAnswer2.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                "b".equals(soal.getSelectedAnswer()) ? R.drawable.ic_check : 0, 0);
        holder.tvAnswer3.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                "c".equals(soal.getSelectedAnswer()) ? R.drawable.ic_check : 0, 0);
        holder.tvAnswer4.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                "d".equals(soal.getSelectedAnswer()) ? R.drawable.ic_check : 0, 0);
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