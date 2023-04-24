package com.example.mystorage.mvvm.user.viewmodel.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.mvvm.user.view.changePassword.ChangeIView

class ChangeViewModelFactory(private val changePasswordIView: ChangeIView): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangeViewModel::class.java))
            return ChangeViewModel(changePasswordIView) as T
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}