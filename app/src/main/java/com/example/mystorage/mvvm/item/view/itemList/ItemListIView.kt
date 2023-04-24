package com.example.mystorage.mvvm.item.view.itemList

import com.example.mystorage.retrofit.response.UserItemResponse
import com.example.mystorage.retrofit.response.ApiResponse

interface ItemListIView {
    fun setGridRecyclerView()
    fun getResponseOnItemLoad()
    fun getResponseOnDelete(itemname: String)
    fun itemLoadResponse(response: UserItemResponse)
    fun itemDeleteResponse(response: ApiResponse)
    fun onItemListSuccess(message: String?)
    fun onItemListError(message: String?)
}