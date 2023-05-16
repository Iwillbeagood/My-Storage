package com.example.mystorage.retrofit.retrofitInterface.userInfo

import com.example.mystorage.retrofit.model.UserHomeInfoResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserHomeInfoLoadApiService {
    @FormUrlEncoded
    @POST("/userHome/load.php")
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun loadUserHomeInfo(
        @Field("userid") userid: String
    ) : Call<UserHomeInfoResponse>
}