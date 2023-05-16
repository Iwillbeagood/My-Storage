package com.example.mystorage.retrofit.model

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
data class UserItem(
    @SerializedName("itemid") val itemid: Int,
    @SerializedName("itemname") val itemname: String?,
    @SerializedName("itemimage") val itemimage: String?,
    @SerializedName("itemplace") val itemplace: String?,
    @SerializedName("itemstore") val itemstore: String?,
    @SerializedName("itemcount") val itemcount: String?,
    @SerializedName("created_at") val created_at: String?
)