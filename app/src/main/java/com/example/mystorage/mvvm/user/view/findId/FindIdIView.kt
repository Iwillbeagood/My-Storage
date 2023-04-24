package com.example.mystorage.mvvm.user.view.findId

interface FindIdIView {
    fun onFindIdSuccess(message: String?)
    fun onFindIdError(message: String?)
    fun phoneTextChange()
}