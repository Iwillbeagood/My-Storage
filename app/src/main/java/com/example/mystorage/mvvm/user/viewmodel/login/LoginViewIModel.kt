package com.example.mystorage.mvvm.user.viewmodel.login

import com.example.mystorage.retrofit.response.ApiResponse

interface LoginViewIModel{
    fun onLogin()
    fun getResponseOnLogin()
    fun userRegistrationResponse(response: ApiResponse)
    fun setAutoLogin(id: String, password: String)
}

