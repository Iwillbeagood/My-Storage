package com.example.mystorage.retrofit.retrofitInterface

import com.example.mystorage.mvvm.model.UserRegistrationResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChangeApiService {
    @FormUrlEncoded
    @POST("changePassword.php")
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun changeUserPassword(
        @Field("userid") userid: String,
        @Field("userpassword") userpassword: String,
        @Field("userphone") userphone: String
    ): Call<UserRegistrationResponse>
}