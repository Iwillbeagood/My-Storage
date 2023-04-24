package com.example.mystorage.retrofit.response

import com.google.gson.annotations.SerializedName

data class UserItemResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: MutableList<UserItem>?
)


