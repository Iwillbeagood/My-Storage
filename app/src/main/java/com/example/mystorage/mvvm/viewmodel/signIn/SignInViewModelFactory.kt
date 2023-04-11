package com.example.mystorage.mvvm.viewmodel.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.mvvm.view.signIn.SignInIView

class SignInViewModelFactory(private val signInView: SignInIView): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java))
            return SignInViewModel(signInView) as T
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}