package com.example.mystorage.mvvm.userhome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.mvvm.userhome.view.UserInformIView

class UserInformViewModelFactory(private val userHomeIView: UserInformIView): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserInformViewModel::class.java))
            return UserInformViewModel(userHomeIView) as T
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}