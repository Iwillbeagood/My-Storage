package com.example.mystorage.mvvm.item.view.listItemClick

import com.example.mystorage.retrofit.model.ApiResponse

interface ListItemClickIView {
    fun getResponseOnDelete(itemid: Int, itemimage: String)
    fun getResponseOnItemCountMinus()
    fun itemResponse(response: ApiResponse)
    fun onItemError(message: String?)
    fun onItemSuccess(message: String?)
    fun getResponseOnUsedItemMove()
}