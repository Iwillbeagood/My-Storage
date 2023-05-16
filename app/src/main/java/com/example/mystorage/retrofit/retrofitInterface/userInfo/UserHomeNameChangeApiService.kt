package com.example.mystorage.retrofit.retrofitInterface.userInfo

import com.example.mystorage.retrofit.model.ApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserHomeNameChangeApiService {
    @FormUrlEncoded
    @POST("/userHome/change-name.php")
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun changeHomeName(
        @Field("userid") userid: String,
        @Field("room_names") roomNames: String,
        @Field("bathroom_names") bathroomNames: String,
        @Field("etc_name") etcName: String
    ): Call<ApiResponse>
}