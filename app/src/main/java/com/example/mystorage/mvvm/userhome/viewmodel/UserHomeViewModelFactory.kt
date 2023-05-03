package com.example.mystorage.mvvm.userhome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.mvvm.userhome.view.UserHomeIView

class UserHomeViewModelFactory(private val userHomeIView: UserHomeIView): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserHomeViewModel::class.java))
            return UserHomeViewModel(userHomeIView) as T
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}