package com.example.mystorage.mvvm.userhome.view.changeUserHomeInfo

import com.example.mystorage.retrofit.response.ApiResponse

interface ChangeUserHomeInfoIView {
    fun getResponseOnUserHomeLoad()
    fun userHomeLoadResponse(roomItems: MutableList<String>?, bathItems: MutableList<String>?, etcName: MutableList<String>?)
    fun userHomeChangeResponse(response: ApiResponse)
    fun onUserHomeInfoChangeSuccess(message: String?)
    fun onUserHomeInfoChangeError(message: String?)
}