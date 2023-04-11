package com.example.mystorage.mvvm.view.signIn

interface SignInIView {
    fun onSignInSuccess(message: String?)
    fun onSignInError(message: String?)
    fun phoneTextChange()
}