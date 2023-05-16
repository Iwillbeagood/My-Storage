package com.example.mystorage.retrofit.retrofitInterface


import com.example.mystorage.mvvm.item.model.ocr.OcrRequest
import com.example.mystorage.mvvm.item.model.ocr.OcrResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NaverOcrApiService {
    @POST("/custom/v1/22318/ff09bb079dd19bcbc95e672a16258f3d0713e0188235943ec171f5088ffaeb41/general/")
    @Headers(
        "accept: application/json",
        "content-type: application/json; charset=utf-8"
    )
    fun ocrGeneral(
        @Header("X-OCR-SECRET") secretKey: String,
        @Body requestBody: OcrRequest
    ): Call<OcrResponse>
}