package com.example.mystorage.mvvm.item.view.itemStructure

import com.example.mystorage.retrofit.response.UserItem
import com.example.mystorage.retrofit.response.UserItemResponse

interface ItemStructureIView {
    fun setRecyclerView()
    fun setSearchSpinner(data : Map<String?, List<UserItem>>)
    fun filterItemList(data : Map<String?, List<UserItem>>, searchPlace: String)
    fun getResponseOnItemLoad()
    fun itemLoadResponse(response: UserItemResponse)
    fun onItemListSuccess(message: String?)
    fun onItemListError(message: String?)
}