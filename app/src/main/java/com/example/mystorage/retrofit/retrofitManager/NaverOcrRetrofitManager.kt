package com.example.mystorage.retrofit.retrofitManager

import com.example.mystorage.retrofit.retrofitInterface.NaverOcrApiService
import com.example.mystorage.retrofit.retrofitInterface.NaverSMSApiService
import com.example.mystorage.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NaverOcrRetrofitManager {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .writeTimeout(100, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://8h1w42l64z.apigw.ntruss.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val naverOcrApiService = retrofit.create(NaverOcrApiService::class.java)

    fun getNaverOcrMApiService(): NaverOcrApiService {
        return naverOcrApiService
    }

}