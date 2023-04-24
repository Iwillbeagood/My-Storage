package com.example.mystorage.mvvm.user.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.mvvm.user.view.login.LoginIView

class LoginViewModelFactory(private val loginIView: LoginIView): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            return LoginViewModel(loginIView) as T
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}