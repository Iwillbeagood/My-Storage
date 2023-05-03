package com.example.mystorage.mvvm.item.viewmodel.itemAdd

import android.graphics.Bitmap
import com.example.mystorage.retrofit.response.ApiResponse
import java.io.File

interface ItemAddIViewModel {
    fun setItemPlace(itemplace: String)
    fun setItemImage(type: String, itemImage: String)
    fun onItemAdd(type: String)
    fun itemResponse(response: ApiResponse)
    fun getResponseOnItemCountUpdate()
    fun getResponseOnItemAdd()
}