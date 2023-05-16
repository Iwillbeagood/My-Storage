package com.example.mystorage.retrofit.retrofitInterface


import com.example.mystorage.retrofit.model.naverSMS.SMSRequest
import com.example.mystorage.retrofit.model.naverSMS.SMSResponse
import retrofit2.Call
import retrofit2.http.*

interface NaverSMSApiService {
    @POST("/sms/v2/services/{serviceId}/messages")
    @Headers(
        "accept: application/json",
        "content-type: application/json; charset=utf-8"
    )
    fun sendSms(
        @Path("serviceId") serviceId: String,
        @Body body: SMSRequest
    ): Call<SMSResponse>
}