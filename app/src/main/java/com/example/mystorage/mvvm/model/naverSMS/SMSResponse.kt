package com.example.mystorage.mvvm.model.naverSMS

data class SMSResponse(
    val requestId: String,
    val requestTime: String,
    val statusCode: String,
    val statusName: String
)