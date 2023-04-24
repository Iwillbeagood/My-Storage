package com.example.mystorage.mvvm.item.viewmodel

import com.example.mystorage.retrofit.response.ApiResponse

interface ItemAddIViewModel {
    fun setItemPlace(itemplace: String)
    fun setItemImage(itemImage: String)
    fun onItemAdd()
    fun getResponseOnItemAdd(status: Boolean)
    fun itemResponse(response: ApiResponse)
}