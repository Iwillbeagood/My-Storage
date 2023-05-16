package com.example.mystorage.mvvm.item.view.usedItem

import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.retrofit.model.UserItemResponse

interface UsedItemIView {
    fun setGridRecyclerView()
    fun getResponseOnUsedItemLoad()
    fun usedItemLoadResponse(response: UserItemResponse)
    fun onUsedItemLoadSuccess(message: String?)
    fun onUsedItemLoadError(message: String?)
    fun getResponseOnUsedItemDeleteAll()
    fun deleteAllUsedItemResponse(response: ApiResponse)
}