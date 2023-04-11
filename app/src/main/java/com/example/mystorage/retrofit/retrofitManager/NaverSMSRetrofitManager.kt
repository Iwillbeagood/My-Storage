package com.example.mystorage.retrofit.retrofitManager

import com.example.mystorage.retrofit.retrofitInterface.NaverSMSApiService
import com.example.mystorage.utils.Constants
import com.example.mystorage.utils.Constants.getSignature
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NaverSMSRetrofitManager {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val timestamp = System.currentTimeMillis()
            val signature = getSignature(timestamp)

            // Retrofit 요청 객체에 필요한 헤더를 추가합니다.
            val request = original.newBuilder()
                .header("x-ncp-apigw-timestamp", timestamp.toString())
                .header("x-ncp-iam-access-key", Constants.ACCESS_KEY_ID)
                .header("x-ncp-apigw-signature-v2", signature!!)
                .method(original.method, original.body)
                .build()

            // 요청을 실행하고 결과를 반환합니다.
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .writeTimeout(100, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://sens.apigw.ntruss.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val naverSMSMApiService = retrofit.create(NaverSMSApiService::class.java)

    fun getNaverSMSMApiService(): NaverSMSApiService {
        return naverSMSMApiService
    }

}