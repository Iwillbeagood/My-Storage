package com.example.mystorage.login.view

interface IView {
    fun onSignInSuccess(message: String?)
    fun onSignInError(message: String?)
}