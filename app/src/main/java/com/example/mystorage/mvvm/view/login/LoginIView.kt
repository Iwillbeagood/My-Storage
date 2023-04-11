package com.example.mystorage.mvvm.view.login

interface LoginIView {
    fun onLoginSuccess(message: String?)
    fun onLoginError(message: String?)
}