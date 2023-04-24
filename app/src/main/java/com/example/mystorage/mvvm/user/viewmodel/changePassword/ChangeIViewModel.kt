package com.example.mystorage.mvvm.user.viewmodel.changePassword

import com.example.mystorage.retrofit.response.ApiResponse

interface ChangeIViewModel {
    fun onChange()
    fun setAuthenticationNum(authenticationNum: String)
    fun getResponseOnChange()
    fun userRegistrationResponse(response: ApiResponse)
}