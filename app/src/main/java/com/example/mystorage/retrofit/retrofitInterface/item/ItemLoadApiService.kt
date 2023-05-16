package com.example.mystorage.retrofit.retrofitInterface.item

import com.example.mystorage.retrofit.model.UserItemResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ItemLoadApiService {
    @FormUrlEncoded
    @POST("/item/load.php")
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun loadItem(
        @Field("userid") userid: String
    ): Call<UserItemResponse>
}