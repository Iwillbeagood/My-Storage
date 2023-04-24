package com.example.mystorage.retrofit.response

import com.google.gson.annotations.SerializedName

data class UserHomeInfoResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("livingroom_names") val livingRoomName: String,
    @SerializedName("kitchen_names") val kitchenName: String,
    @SerializedName("storage_names") val storageName: String,
    @SerializedName("room_names") val roomNames: String,
    @SerializedName("bathroom_names") val bathroomNames: String,
    @SerializedName("etc_name") val etc_name: String
)
