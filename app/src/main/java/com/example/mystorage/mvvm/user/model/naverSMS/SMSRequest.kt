package com.example.mystorage.mvvm.user.model.naverSMS

data class SMSRequest(
    val type: String,
    val contentType: String,
    val countryCode: String,
    val from: String,
    val content: String,
    val messages: List<Message>,
)