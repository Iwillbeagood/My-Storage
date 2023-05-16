package com.example.mystorage.mvvm.userHome.view.changeStrName

import androidx.recyclerview.widget.RecyclerView
import com.example.mystorage.retrofit.model.ApiResponse

interface ChangeStrNameIView {
    fun getResponseOnUserHomeLoad()
    fun userHomeLoadResponse(roomItems: MutableList<String>?, bathItems: MutableList<String>?, etcItems: MutableList<String>?)
    fun userHomeChangeResponse(response: ApiResponse)
    fun onUserHomeInfoChangeSuccess(message: String?)
    fun onUserHomeInfoChangeError(message: String?)
    fun getJsonStringFromRecyclerView(recyclerView: RecyclerView)
    fun changeNameOfHome(jsonStringRoom: String, jsonStringBath: String, jsonStringEtc: String)
}