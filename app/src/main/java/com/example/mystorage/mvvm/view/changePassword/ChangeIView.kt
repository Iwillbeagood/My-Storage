package com.example.mystorage.mvvm.view.changePassword

interface ChangeIView {
    fun onChangeSuccess(message: String?)
    fun onChangeError(message: String?)
    fun phoneTextChange()
}