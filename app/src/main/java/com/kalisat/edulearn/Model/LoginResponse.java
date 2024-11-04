package com.kalisat.edulearn.Model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private boolean status;
    private String message;
    private String role;

    @SerializedName("user")
    private User user;

    // Getter dan Setter
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

    public String getRole() {  // Getter role menggunakan String
        return role;
    }

    public void setRole(String role) {  // Setter role menggunakan String
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class User {
        private int id;
        private String nama;
        private String email;
        private String nomor_hp;

        // Getter dan Setter
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
            return nomor_hp;
        }

        public void setNomorHp(String nomor_hp) {
            this.nomor_hp = nomor_hp;
        }
    }
}
