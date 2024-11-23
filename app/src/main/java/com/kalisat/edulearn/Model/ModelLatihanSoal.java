package com.kalisat.edulearn.Model;

public class ModelLatihanSoal {
    private int id; // Tambahkan id
    private String judulSoal;
    private String tanggalSoal;
    private String deadline;

    public ModelLatihanSoal(int id, String judulSoal, String tanggalSoal, String deadline) {
        this.id = id;
        this.judulSoal = judulSoal;
        this.tanggalSoal = tanggalSoal;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudulSoal() {
        return judulSoal;
    }

    public void setJudulSoal(String judulSoal) {
        this.judulSoal = judulSoal;
    }

    public String getTanggalSoal() {
        return tanggalSoal;
    }

    public void setTanggalSoal(String tanggalSoal) {
        this.tanggalSoal = tanggalSoal;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
