package com.example.mystorage.retrofit.retrofitManager

import com.example.mystorage.retrofit.retrofitInterface.ChangeApiService
import com.example.mystorage.retrofit.retrofitInterface.FindIdApiService
import com.example.mystorage.retrofit.retrofitInterface.LoginApiService
import com.example.mystorage.retrofit.retrofitInterface.SignInApiService
import com.example.mystorage.utils.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

class UserRetrofitManager private constructor() {
    companion object {
        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("http://${Constants.IP_ADDRESS}/android/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(okHttpClient)
            .build()


        fun getSignInApiService(): SignInApiService {
            return retrofit.create(SignInApiService::class.java)
        }

        fun getLoginApiService(): LoginApiService {
            return retrofit.create(LoginApiService::class.java)
        }

        fun getFindIdApiService(): FindIdApiService {
            return retrofit.create(FindIdApiService::class.java)
        }

        fun getChangeApiService(): ChangeApiService {
            return retrofit.create(ChangeApiService::class.java)
        }
    }
}