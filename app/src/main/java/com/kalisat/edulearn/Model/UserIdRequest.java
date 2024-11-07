package com.kalisat.edulearn.Model;

public class UserIdRequest {
    private int user_id;
    private String role; // Tambahkan parameter role

    public UserIdRequest(int user_id, String role) {
        this.user_id = user_id;
        this.role = role;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
