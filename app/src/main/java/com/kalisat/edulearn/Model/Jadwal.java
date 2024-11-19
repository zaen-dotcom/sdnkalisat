package com.kalisat.edulearn.Model;

public class Jadwal {
    private String hari;
    private String mapel1, waktu1;
    private String mapel2, waktu2;
    private String mapel3, waktu3;

    public Jadwal(String hari, String mapel1, String waktu1, String mapel2, String waktu2, String mapel3, String waktu3) {
        this.hari = hari;
        this.mapel1 = mapel1;
        this.waktu1 = waktu1;
        this.mapel2 = mapel2;
        this.waktu2 = waktu2;
        this.mapel3 = mapel3;
        this.waktu3 = waktu3;
    }

    public String getHari() {
        return hari;
    }

    public String getMapel1() {
        return mapel1;
    }

    public String getWaktu1() {
        return waktu1;
    }

    public String getMapel2() {
        return mapel2;
    }

    public String getWaktu2() {
        return waktu2;
    }

    public String getMapel3() {
        return mapel3;
    }

    public String getWaktu3() {
        return waktu3;
    }
}
