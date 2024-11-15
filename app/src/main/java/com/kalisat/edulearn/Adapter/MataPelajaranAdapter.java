package com.kalisat.edulearn.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kalisat.edulearn.Activity.DetailMapelActivity;
import com.kalisat.edulearn.Model.MataPelajaran;
import com.kalisat.edulearn.R;

import java.util.List;

public class MataPelajaranAdapter extends RecyclerView.Adapter<MataPelajaranAdapter.MataPelajaranViewHolder> {

    private List<MataPelajaran> mataPelajaranList;
    private Context context;

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

        holder.tvJudulMapel.setText(mataPelajaran.getNamaMapel());
        holder.tvNamaGuru.setText(mataPelajaran.getNamaGuru());

        // Set OnClickListener pada item untuk membuka DetailMapelActivity
        holder.itemView.setOnClickListener(v -> {
            // Kirim data id_mapel dan nama_mapel ke DetailMapelActivity
            Intent intent = new Intent(context, DetailMapelActivity.class);
            intent.putExtra("id_mapel", mataPelajaran.getId());  // Mengirim ID mata pelajaran
            intent.putExtra("nama_mapel", mataPelajaran.getNamaMapel());  // Mengirim nama mata pelajaran
            context.startActivity(intent); // Memulai activity baru
        });
    }


    @Override
    public int getItemCount() {
        return mataPelajaranList.size();
    }

    public class MataPelajaranViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudulMapel, tvNamaGuru;
        Button btnMasukKelas;
        ImageView imageViewBackground;

        public MataPelajaranViewHolder(@NonNull View itemView) {
            super(itemView);
            // Pastikan id sesuai dengan yang ada di item_kelas.xml
            tvJudulMapel = itemView.findViewById(R.id.tv_judul_mapel);
            tvNamaGuru = itemView.findViewById(R.id.tv_namaguru);
            btnMasukKelas = itemView.findViewById(R.id.btn_masukkelas);
            imageViewBackground = itemView.findViewById(R.id.imageView10);
        }
    }
}
