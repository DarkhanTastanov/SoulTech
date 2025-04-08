package com.example.myfaith.api

import com.example.myfaith.entity.LoginResponse
import com.example.myfaith.entity.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {
    @POST("users/login/")
    @FormUrlEncoded
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

}
