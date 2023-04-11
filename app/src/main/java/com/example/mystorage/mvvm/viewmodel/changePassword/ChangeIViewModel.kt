package com.example.mystorage.mvvm.viewmodel.changePassword

import com.example.mystorage.mvvm.model.UserRegistrationResponse

interface ChangeIViewModel {
    fun onChange()
    fun setAuthenticationNum(authenticationNum: String)
    fun getResponseOnChange()
    fun userRegistrationResponse(response: UserRegistrationResponse)
}