package com.example.myfaith.datasource

import RegistrationApi
import android.content.Context
import com.example.myfaith.api.LoginApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Request

object ApiSource {
    private var appContext: Context? = null
    private val plainClient = OkHttpClient.Builder().build()

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private const val BASE_URL = "https://94b8-176-64-5-40.ngrok-free.app"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token = appContext?.let { getToken(it) }

            val request: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
        .build()

    private fun createRetrofitService(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val login: LoginApi by lazy {
        createRetrofitService(BASE_URL).create(LoginApi::class.java)
    }

    private fun createPlainRetrofitService(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(plainClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val registration: RegistrationApi by lazy {
        createPlainRetrofitService(BASE_URL).create(RegistrationApi::class.java)
    }

    private fun getToken(context: android.content.Context): String {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", android.content.Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }
}