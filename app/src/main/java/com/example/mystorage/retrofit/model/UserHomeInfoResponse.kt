package com.example.mystorage.retrofit.model

import com.google.gson.annotations.SerializedName

data class UserHomeInfoResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("livingroom") val livingRoom: String,
    @SerializedName("kitchen") val kitchen: String,
    @SerializedName("storage") val storage: String,
    @SerializedName("room_names") val roomNames: String,
    @SerializedName("bathroom_names") val bathroomNames: String,
    @SerializedName("etc_name") val etc_name: String
)
