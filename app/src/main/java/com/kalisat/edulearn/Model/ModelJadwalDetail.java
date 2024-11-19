package com.kalisat.edulearn.Model;

public class ModelJadwalDetail {
    private String namaMapel;
    private String jamMulai;
    private String jamSelesai;

    public ModelJadwalDetail(String namaMapel, String jamMulai, String jamSelesai) {
        this.namaMapel = namaMapel;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public String getJamMulai() {
        return jamMulai;
    }

    public String getJamSelesai() {
        return jamSelesai;
    }
}
