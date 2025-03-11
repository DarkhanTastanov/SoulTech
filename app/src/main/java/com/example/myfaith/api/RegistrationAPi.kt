package com.example.myfaith.api

import com.example.myfaith.entity.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RegistrationApi {
    @GET("/users/registration")
    fun getUserRegistration(@Header("Authorization") authHeader: String): Call<List<User>>
}
