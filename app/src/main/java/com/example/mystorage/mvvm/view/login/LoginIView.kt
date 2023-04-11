package com.example.mystorage.mvvm.view.login

interface LoginIView {
    fun onLoginSuccess(message: String?, id: String, password: String)
    fun onLoginError(message: String?)
    fun checkAutoLogin()
}