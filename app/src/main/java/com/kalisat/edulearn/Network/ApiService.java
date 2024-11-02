package com.kalisat.edulearn.Network;

import com.kalisat.edulearn.Model.LoginRequest;
import com.kalisat.edulearn.Model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login_api.php")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
