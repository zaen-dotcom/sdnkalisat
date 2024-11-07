package com.kalisat.edulearn.Model;

public class UserResponse {
    private boolean status;
    private String message;
    private UserData user;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public UserData getUser() {
        return user;
    }

    // Inner class untuk struktur data user
    public class UserData {
        private String nama;

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }
    }
}
