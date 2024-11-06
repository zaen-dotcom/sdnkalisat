package com.kalisat.edulearn.Model;

public class ChangePasswordRequest {
    private int user_id; // Tambahkan user_id jika diperlukan dalam API Anda
    private String current_password;
    private String new_password;

    public ChangePasswordRequest(int user_id, String currentPassword, String newPassword) {
        this.user_id = user_id;
        this.current_password = currentPassword;
        this.new_password = newPassword;
    }

    // Getters and Setters (jika diperlukan)
    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getCurrentPassword() {
        return current_password;
    }

    public void setCurrentPassword(String current_password) {
        this.current_password = current_password;
    }

    public String getNewPassword() {
        return new_password;
    }

    public void setNewPassword(String new_password) {
        this.new_password = new_password;
    }
}
