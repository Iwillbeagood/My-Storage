package com.example.mystorage.retrofit.retrofitInterface.usedItem

import com.example.mystorage.retrofit.model.UserItemResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface UsedItemLoadApiService {
    @FormUrlEncoded
    @POST("/used-item/load.php")
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun loadUsedItem(
        @Field("userid") userid: String
    ): Call<UserItemResponse>
}