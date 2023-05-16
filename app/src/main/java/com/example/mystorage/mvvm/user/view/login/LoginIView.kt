package com.example.mystorage.mvvm.user.view.login

import com.example.mystorage.retrofit.model.ApiResponse

interface LoginIView {
    fun onLoginSuccess(message: String?, id: String, password: String)
    fun onLoginError(message: String?)
    fun checkAutoLogin()
    fun getResponseOnUserHomeInfoCheck()
    fun userHomeInfoCheckResponse(response: ApiResponse)
}