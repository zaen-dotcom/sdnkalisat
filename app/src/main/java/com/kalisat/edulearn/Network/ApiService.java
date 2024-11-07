package com.kalisat.edulearn.Network;

import com.kalisat.edulearn.Model.ChangePasswordRequest;
import com.kalisat.edulearn.Model.ChangePasswordResponse;
import com.kalisat.edulearn.Model.LoginRequest;
import com.kalisat.edulearn.Model.LoginResponse;
import com.kalisat.edulearn.Model.UserIdRequest;
import com.kalisat.edulearn.Model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login_api.php")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("changepw_api.php") // Pastikan endpoint ini sesuai dengan file yang Anda miliki
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest request);

    @POST("get_username_api.php")
    Call<UserResponse> getUserName(@Body UserIdRequest userIdRequest);
}
