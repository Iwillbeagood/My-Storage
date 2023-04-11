package com.example.mystorage.mvvm.viewmodel.findId

import com.example.mystorage.mvvm.model.UserRegistrationResponse

interface FindIdIViewModel {
    fun onFindId()
    fun getResponseOnFindId()
    fun setAuthenticationNum(authenticationNum: String)
    fun userRegistrationResponse(response: UserRegistrationResponse)

}