package com.example.mystorage.retrofit.retrofitInterface.item

import com.example.mystorage.retrofit.model.ApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ItemDeleteAllApiService {
    @FormUrlEncoded
    @POST("/item/delete-all.php")
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun deleteAllItem(
        @Field("userid") userid: String,
        @Field("table") table: String
    ): Call<ApiResponse>
}