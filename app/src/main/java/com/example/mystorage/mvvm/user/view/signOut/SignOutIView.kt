package com.example.mystorage.mvvm.user.view.signOut

import com.example.mystorage.retrofit.model.ApiResponse

interface SignOutIView {
    fun onSignOutSuccess(message: String?)
    fun onSignOutError(message: String?)
    fun onSignOut()
    fun userSignOutResponse(response: ApiResponse)
}