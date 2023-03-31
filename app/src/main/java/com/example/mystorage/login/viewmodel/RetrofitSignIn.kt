package com.example.mystorage.login.viewmodel

import com.example.mystorage.login.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitSignIn {
    @POST("android_signin_php.php")
    fun registerUser(@Body user: User): Call<ResponseBody>
}