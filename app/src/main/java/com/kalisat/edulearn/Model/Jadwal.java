package com.kalisat.edulearn.Model;

public class Jadwal {
    private String hari;
    private String mapel1, waktu1;
    private String mapel2, waktu2;
    private String mapel3, waktu3;

    // Constructor
    public Jadwal(String hari, String mapel1, String waktu1, String mapel2, String waktu2, String mapel3, String waktu3) {
        this.hari = hari;
        this.mapel1 = mapel1;
        this.waktu1 = waktu1;
        this.mapel2 = mapel2;
        this.waktu2 = waktu2;
        this.mapel3 = mapel3;
        this.waktu3 = waktu3;
    }

    // Default constructor (tambahkan jika Anda perlu membuat objek kosong terlebih dahulu)
    public Jadwal() {
    }

    // Getter untuk Hari
    public String getHari() {
        return hari;
    }

    // Setter untuk Hari
    public void setHari(String hari) {
        this.hari = hari;
    }

    // Getter untuk Mapel1
    public String getMapel1() {
        return mapel1;
    }

    // Setter untuk Mapel1
    public void setMapel1(String mapel1) {
        this.mapel1 = mapel1;
    }

    // Getter untuk Waktu1
    public String getWaktu1() {
        return waktu1;
    }

    // Setter untuk Waktu1
    public void setWaktu1(String waktu1) {
        this.waktu1 = waktu1;
    }

    // Getter untuk Mapel2
    public String getMapel2() {
        return mapel2;
    }

    // Setter untuk Mapel2
    public void setMapel2(String mapel2) {
        this.mapel2 = mapel2;
    }

    // Getter untuk Waktu2
    public String getWaktu2() {
        return waktu2;
    }

    // Setter untuk Waktu2
    public void setWaktu2(String waktu2) {
        this.waktu2 = waktu2;
    }

    // Getter untuk Mapel3
    public String getMapel3() {
        return mapel3;
    }

    // Setter untuk Mapel3
    public void setMapel3(String mapel3) {
        this.mapel3 = mapel3;
    }

    // Getter untuk Waktu3
    public String getWaktu3() {
        return waktu3;
    }

    // Setter untuk Waktu3
    public void setWaktu3(String waktu3) {
        this.waktu3 = waktu3;
    }
}
