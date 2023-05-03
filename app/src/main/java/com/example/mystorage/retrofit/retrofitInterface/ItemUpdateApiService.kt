package com.example.mystorage.retrofit.retrofitInterface

import com.example.mystorage.retrofit.response.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ItemUpdateApiService {
    @Multipart
    @POST("/item/update.php")
    fun updateItem(
        @Part("userid") userid: RequestBody,
        @Part("itemname") itemname: RequestBody,
        @Part itemimage: MultipartBody.Part?,
        @Part("itemplace") itemplace: RequestBody,
        @Part("itemstore") itemstore: RequestBody,
        @Part("itemcount") itemcount: RequestBody,
        @Part("originname") originname: RequestBody,
        @Part("originimage") originimage: RequestBody
    ): Call<ApiResponse>
}