package com.kalisat.edulearn.Model;

public class ModelTugas {
    private int id; // ID Tugas
    private String judulTugas; // Judul tugas
    private String deskripsi; // Deskripsi tugas
    private String tanggalTugas; // Tanggal tugas
    private String deadline; // Deadline tugas

    // Constructor lengkap
    public ModelTugas(int id, String judulTugas, String deskripsi, String tanggalTugas, String deadline) {
        this.id = id;
        this.judulTugas = judulTugas;
        this.deskripsi = deskripsi;
        this.tanggalTugas = tanggalTugas;
        this.deadline = deadline;
    }

    // Getter dan Setter untuk ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter dan Setter untuk Judul Tugas
    public String getJudulTugas() {
        return judulTugas;
    }

    public void setJudulTugas(String judulTugas) {
        this.judulTugas = judulTugas;
    }

    // Getter dan Setter untuk Deskripsi
    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    // Getter dan Setter untuk Tanggal Tugas
    public String getTanggalTugas() {
        return tanggalTugas;
    }

    public void setTanggalTugas(String tanggalTugas) {
        this.tanggalTugas = tanggalTugas;
    }

    // Getter dan Setter untuk Deadline
    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
