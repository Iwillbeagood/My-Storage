package com.example.mystorage.mvvm.user.viewmodel.findId

import com.example.mystorage.retrofit.response.ApiResponse

interface FindIdIViewModel {
    fun onFindId()
    fun getResponseOnFindId()
    fun setAuthenticationNum(authenticationNum: String)
    fun userRegistrationResponse(response: ApiResponse)

}