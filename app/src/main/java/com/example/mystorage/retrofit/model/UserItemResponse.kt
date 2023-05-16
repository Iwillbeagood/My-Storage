package com.example.mystorage.retrofit.model

import com.google.gson.annotations.SerializedName

data class UserItemResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: MutableList<UserItem>?
)


