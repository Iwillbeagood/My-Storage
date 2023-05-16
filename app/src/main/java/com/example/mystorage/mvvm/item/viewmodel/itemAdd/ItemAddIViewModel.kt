package com.example.mystorage.mvvm.item.viewmodel.itemAdd

import com.example.mystorage.retrofit.model.ApiResponse

interface ItemAddIViewModel {
    fun setItemPlace(itemplace: String)
    fun setItemImage(type: String, itemImage: String)
    fun onItemAdd(type: String, itemid: Int)
    fun itemResponse(response: ApiResponse)
    fun getResponseOnItemCountUpdate(itemid: Int)
    fun getResponseOnItemAdd()
}