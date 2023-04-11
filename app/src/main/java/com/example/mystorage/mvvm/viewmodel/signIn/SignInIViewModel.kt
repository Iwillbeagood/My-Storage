package com.example.mystorage.mvvm.viewmodel.signIn

import com.example.mystorage.mvvm.model.UserRegistrationResponse

interface SignInIViewModel {
    fun onSignIn()
    fun setAuthenticationNum(authenticationNum: String)
    fun getResponseOnSignIn()
    fun userRegistrationResponse(response: UserRegistrationResponse)
}