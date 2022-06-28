package com.sliide.usermanager.api

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitFactory(
    private val baseUrl: String,
    private val token: String
) {
    private var authorization = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", token)
            .build()
        chain.proceed(newRequest)
    }.build()

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .client(authorization)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
