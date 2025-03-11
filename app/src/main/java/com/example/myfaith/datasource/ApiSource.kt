package com.example.myfaith.datasource

import android.content.Context
import com.example.myfaith.api.LoginApi
import com.example.myfaith.api.RegistrationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Request

object ApiSource {
    private var appContext: Context? = null
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private const val BASE_URL = ""

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
    val registration: RegistrationApi by lazy {
        createRetrofitService(BASE_URL).create(RegistrationApi::class.java)
    }
    val login: LoginApi by lazy {
        createRetrofitService(BASE_URL).create(LoginApi::class.java)
    }
    private fun getToken(context: android.content.Context): String {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", android.content.Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }
}