package com.example.myapplication.server;

import com.example.myapplication.data.ClientInformation;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface RestAPICommunication {

        //Login
        @POST("login")
        Call<ResponseLogin> login(@Body RequestLogin req);

        //Register
        @POST("register")
        Call<ResponseRegister> registerUser(@Body RequestRegister req);

        //Save workout
        @POST("workout")
        Call<ResponseSaveWorkout> saveWorkout(@Body RequestSaveWorkout req, @Header("Authorization") String token);



    }
