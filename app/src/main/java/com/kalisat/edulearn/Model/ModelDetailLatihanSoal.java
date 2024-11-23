package com.kalisat.edulearn.Model;

public class ModelDetailLatihanSoal {
    private String judulSoal;
    private int jumlahSoal;
    private String tanggalSoal;
    private String deadline;
    private int id;

    public ModelDetailLatihanSoal(String judulSoal, int jumlahSoal, String tanggalSoal, String deadline, int id) {
        this.judulSoal = judulSoal;
        this.jumlahSoal = jumlahSoal;
        this.tanggalSoal = tanggalSoal;
        this.deadline = deadline;
        this.id = id;
    }

    public String getJudulSoal() {
        return judulSoal;
    }

    public void setJudulSoal(String judulSoal) {
        this.judulSoal = judulSoal;
    }

    public int getJumlahSoal() {
        return jumlahSoal;
    }

    public void setJumlahSoal(int jumlahSoal) {
        this.jumlahSoal = jumlahSoal;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
