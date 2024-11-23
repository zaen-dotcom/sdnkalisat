package com.kalisat.edulearn.Model;

public class ModelSoal {
    private int id;
    private String soal;
    private String pilihanA;
    private String pilihanB;
    private String pilihanC;
    private String pilihanD;
    private String jawaban;

    public ModelSoal(int id, String soal, String pilihanA, String pilihanB, String pilihanC, String pilihanD, String jawaban) {
        this.id = id;
        this.soal = soal;
        this.pilihanA = pilihanA;
        this.pilihanB = pilihanB;
        this.pilihanC = pilihanC;
        this.pilihanD = pilihanD;
        this.jawaban = jawaban;
    }

    public int getId() {
        return id;
    }

    public String getSoal() {
        return soal;
    }

    public String getPilihanA() {
        return pilihanA;
    }

    public String getPilihanB() {
        return pilihanB;
    }

    public String getPilihanC() {
        return pilihanC;
    }

    public String getPilihanD() {
        return pilihanD;
    }

    public String getJawaban() {
        return jawaban;
    }
}
