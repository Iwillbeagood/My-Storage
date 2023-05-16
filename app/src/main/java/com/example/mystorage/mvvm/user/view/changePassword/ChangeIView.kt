package com.example.mystorage.mvvm.user.view.changePassword

interface ChangeIView {
    fun onChangeSuccess(message: String?)
    fun onChangeError(message: String?)
    fun phoneTextChange()
    fun sendSMS()
}