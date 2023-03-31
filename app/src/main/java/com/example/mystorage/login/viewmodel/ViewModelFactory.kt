package com.example.mystorage.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.login.view.IView

class ViewModelFactory(
    private val signInView: IView
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java))
            return SignInViewModel(signInView) as T
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}