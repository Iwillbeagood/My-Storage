package com.example.mystorage.mvvm.user.view.signIn

interface SignInIView {
    fun onSignInSuccess(message: String?)
    fun onSignInError(message: String?)
    fun phoneTextChange()
    fun sendSMS()
}