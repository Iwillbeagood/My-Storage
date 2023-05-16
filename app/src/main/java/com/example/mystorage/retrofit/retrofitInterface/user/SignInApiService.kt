package com.example.mystorage.retrofit.retrofitInterface.user

import com.example.mystorage.retrofit.model.ApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignInApiService {
    @FormUrlEncoded
    @POST("user/signIn.php")
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun registerUser(
        @Field("username") username: String,
        @Field("userid") userid: String,
        @Field("userpassword") userpassword: String,
        @Field("userphone") userphone: String,
    ): Call<ApiResponse>
}