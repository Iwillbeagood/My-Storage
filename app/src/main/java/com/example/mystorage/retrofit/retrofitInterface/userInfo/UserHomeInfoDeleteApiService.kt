package com.example.mystorage.retrofit.retrofitInterface.userInfo

import com.example.mystorage.retrofit.model.ApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserHomeInfoDeleteApiService {
    @FormUrlEncoded
    @POST("userHome/delete.php")
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun userHomeInfoDelete(
        @Field("userid") userid: String
    ) : Call<ApiResponse>
}