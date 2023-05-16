package com.example.mystorage.retrofit.retrofitInterface.user

import com.example.mystorage.retrofit.model.ApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignOutApiService {
    @FormUrlEncoded
    @POST("/item/signOut.php")
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun signOutUser(
        @Field("userid") userid: String,
        @Field("userpassword") userpassword: String
    ): Call<ApiResponse>
}