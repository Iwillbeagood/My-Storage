package com.example.mystorage.mvvm.viewmodel.login

import com.example.mystorage.mvvm.model.UserRegistrationResponse

interface LoginViewIModel{
    fun onLogin()
    fun getResponseOnLogin()
    fun userRegistrationResponse(response: UserRegistrationResponse)
}

