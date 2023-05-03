package com.example.mystorage.mvvm.item.viewmodel.itemUpdate

import com.example.mystorage.retrofit.response.ApiResponse

interface ItemUpdateIViewModel {
    fun setItemPlace(itemplace: String)
    fun setItemImage(type: String, itemImage: String)
    fun onItemUpdate()
    fun getResponseOnItemUpdate()
    fun itemUpdateResponse(response: ApiResponse)
    fun setOriginItemInfo(
        itemname: String,
        itemstore: String,
        originname: String,
        originimage: String
    )
}