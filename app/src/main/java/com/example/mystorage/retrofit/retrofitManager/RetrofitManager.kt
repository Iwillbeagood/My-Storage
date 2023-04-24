package com.example.mystorage.retrofit.retrofitManager

import com.example.mystorage.retrofit.retrofitInterface.*
import com.example.mystorage.utils.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitManager private constructor() {
    companion object {
        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("http://${Constants.IP_ADDRESS}/")
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

        fun getItemAddApiService(): ItemAddApiService {
            return retrofit.create(ItemAddApiService::class.java)
        }

        fun getItemLoadApiService(): ItemLoadApiService {
            return retrofit.create(ItemLoadApiService::class.java)
        }

        fun getItemUpdateApiService(): ItemUpdateApiService {
            return retrofit.create(ItemUpdateApiService::class.java)
        }

        fun getItemDeleteApiService(): ItemDeleteApiService {
            return retrofit.create(ItemDeleteApiService::class.java)
        }

        fun getItemCountUpdateApiService(): ItemCountUpdateApiService {
            return retrofit.create(ItemCountUpdateApiService::class.java)
        }

        fun getUserInfoHomeAddApiService(): UserHomeInfoAddApiService {
            return retrofit.create(UserHomeInfoAddApiService::class.java)
        }

        fun getUserInfoHomeCheckApiService(): UserHomeInfoCheckApiService {
            return retrofit.create(UserHomeInfoCheckApiService::class.java)
        }

        fun getUserHomeInfoLoadApiService(): UserHomeInfoLoadApiService {
            return retrofit.create(UserHomeInfoLoadApiService::class.java)
        }
    }
}