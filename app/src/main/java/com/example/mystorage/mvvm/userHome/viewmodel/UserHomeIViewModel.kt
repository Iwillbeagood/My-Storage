package com.example.mystorage.mvvm.userHome.viewmodel

import androidx.lifecycle.LiveData
import com.example.mystorage.retrofit.model.ApiResponse

interface UserHomeIViewModel {
    fun getRoomNum(): LiveData<Int>
    fun getBathNum(): LiveData<Int>
    fun getUserHomeAddResult(): LiveData<Pair<Boolean, String>>
    fun setUserHome(livingroom: String, kitchen: String, storage: String)
    fun setUserHomeNum(roomNum: Int, bathNum: Int)
    fun setUserName(roomNames: String, bathroomNames: String, etcName: String)
    fun onUserInformAdd()
    fun getResponseOnUserHomeAdd()
    fun userHomeResponse(response: ApiResponse)
}