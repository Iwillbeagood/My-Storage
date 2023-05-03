package com.example.mystorage.retrofit.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
data class UserItem(
    @SerializedName("itemname") val itemname: String?,
    @SerializedName("itemimage") val itemimage: String?,
    @SerializedName("itemplace") val itemplace: String?,
    @SerializedName("itemstore") val itemstore: String?,
    @SerializedName("itemcount") val itemcount: String?
)