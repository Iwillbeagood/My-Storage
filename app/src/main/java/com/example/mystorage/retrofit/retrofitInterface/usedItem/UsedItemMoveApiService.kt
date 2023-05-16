package com.example.mystorage.retrofit.retrofitInterface.usedItem

import com.example.mystorage.retrofit.model.ApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface UsedItemMoveApiService {
    @FormUrlEncoded
    @POST("/used-item/move.php")
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    fun moveUsedItem(
        @Field("userid") userid: String,
        @Field("usedItemid") usedItemid: Int
    ): Call<ApiResponse>
}