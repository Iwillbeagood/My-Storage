package com.example.mystorage.retrofit.retrofitManager

import com.example.mystorage.retrofit.retrofitInterface.item.*
import com.example.mystorage.retrofit.retrofitInterface.usedItem.UsedItemLoadApiService
import com.example.mystorage.retrofit.retrofitInterface.item.ItemMoveApiService
import com.example.mystorage.retrofit.retrofitInterface.usedItem.UsedItemMoveApiService
import com.example.mystorage.retrofit.retrofitInterface.user.*
import com.example.mystorage.retrofit.retrofitInterface.userInfo.*
import com.example.mystorage.utils.Constants.IP_ADDRESS
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
            .baseUrl("http://${IP_ADDRESS}/")
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

        fun getSignOutApiService(): SignOutApiService {
            return retrofit.create(SignOutApiService::class.java)
        }

        fun getUserInfoApiService(): UserInfoApiService {
            return retrofit.create(UserInfoApiService::class.java)
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

        fun getItemPlaceUpdateApiService(): ItemPlaceUpdateApiService {
            return retrofit.create(ItemPlaceUpdateApiService::class.java)
        }

        fun getItemDeleteApiService(): ItemDeleteApiService {
            return retrofit.create(ItemDeleteApiService::class.java)
        }

        fun getItemDeleteAllApiService(): ItemDeleteAllApiService {
            return retrofit.create(ItemDeleteAllApiService::class.java)
        }

        fun getItemCountUpdateApiService(): ItemCountUpdateApiService {
            return retrofit.create(ItemCountUpdateApiService::class.java)
        }

        fun getItemMoveApiService(): ItemMoveApiService {
            return retrofit.create(ItemMoveApiService::class.java)
        }

        fun getUsedItemLoadApiService(): UsedItemLoadApiService {
            return retrofit.create(UsedItemLoadApiService::class.java)
        }

        fun getUsedItemMoveApiService(): UsedItemMoveApiService {
            return retrofit.create(UsedItemMoveApiService::class.java)
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

        fun getUserHomeNameChangeApiService(): UserHomeNameChangeApiService {
            return retrofit.create(UserHomeNameChangeApiService::class.java)
        }

        fun getUserHomeInfoDeleteApiService(): UserHomeInfoDeleteApiService {
            return retrofit.create(UserHomeInfoDeleteApiService::class.java)
        }
    }
}