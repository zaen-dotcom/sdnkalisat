package com.kalisat.edulearn.Model;

public class MataPelajaran {
    private int id;
    private String nama_mapel;
    private String nama_guru;

    public MataPelajaran(int id, String nama_mapel, String nama_guru) {
        this.id = id;
        this.nama_mapel = nama_mapel;
        this.nama_guru = nama_guru;
    }

    public int getId() {
        return id;
    }

    public String getNamaMapel() {
        return nama_mapel;
    }

    public String getNamaGuru() {
        return nama_guru;
    }
}
