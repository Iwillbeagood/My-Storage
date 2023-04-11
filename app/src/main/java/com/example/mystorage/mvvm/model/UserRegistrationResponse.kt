package com.example.mystorage.mvvm.model

import com.google.gson.annotations.SerializedName

data class UserRegistrationResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)