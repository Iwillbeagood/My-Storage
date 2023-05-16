package com.example.mystorage.mvvm.item.view.usedItemClick

import com.example.mystorage.retrofit.model.ApiResponse

interface UsedItemClickIView {
    fun usedItemResponse(response: ApiResponse)
    fun getResponseOnUsedItemDelete()
    fun onUsedItemSuccess(message: String?)
    fun onUsedItemError(message: String?)
    fun getResponseOnUsedItemMove()
}