package com.example.mystorage.mvvm.item.view.itemList

import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.retrofit.model.UserItemResponse

interface ItemListIView {
    fun setGridRecyclerView()
    fun filterItemList(searchText: String)
    fun getResponseOnItemLoad()
    fun itemLoadResponse(response: UserItemResponse)
    fun onItemListSuccess(message: String?)
    fun onItemListError(message: String?)
    fun getResponseOnDelete(itemid: Int, itemimage: String)
    fun deleteSelectedItem()
    fun getResponseOnUpdateItemplace(itemid: Int, itemplace: String)
    fun updateItemplace(itemplace: String)
    fun responseOnItem(response: ApiResponse)
}