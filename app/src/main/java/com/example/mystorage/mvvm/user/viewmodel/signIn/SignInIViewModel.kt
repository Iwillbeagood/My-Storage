package com.example.mystorage.mvvm.user.viewmodel.signIn

import com.example.mystorage.retrofit.model.ApiResponse

interface SignInIViewModel {
    fun onSignIn()
    fun setAuthenticationNum(authenticationNum: String)
    fun getResponseOnSignIn()
    fun userRegistrationResponse(response: ApiResponse)
}