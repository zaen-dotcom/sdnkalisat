package com.kalisat.edulearn.Model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private boolean status;
    private String message;
    private String role;

    @SerializedName("user")
    private User user;

    // Getter dan Setter untuk LoginResponse
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Inner class untuk User
    public static class User {
        private int id;
        private String nama;
        private String email;
        @SerializedName("nomor_hp")
        private String nomorHp; // Sesuaikan nama dengan API
        @SerializedName("kelas_mapel")
        private String kelasMapel; // Untuk guru atau siswa
        @SerializedName("nisn_nipd")
        private String nisnOrNipd; // NISN untuk siswa atau NIPD untuk guru

        // Getter dan Setter untuk User
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNomorHp() {
            return nomorHp;
        }

        public void setNomorHp(String nomorHp) {
            this.nomorHp = nomorHp;
        }

        public String getKelasMapel() {
            return kelasMapel;
        }

        public void setKelasMapel(String kelasMapel) {
            this.kelasMapel = kelasMapel;
        }

        public String getNisnOrNipd() {
            return nisnOrNipd;
        }

        public void setNisnOrNipd(String nisnOrNipd) {
            this.nisnOrNipd = nisnOrNipd;
        }
    }
}
