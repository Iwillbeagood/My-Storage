package com.example.mystorage.mvvm.viewmodel.findId

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.mvvm.view.findId.FindIdIView

class FindIdViewModelFactory(private val findIdIView: FindIdIView): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindIdViewModel::class.java))
            return FindIdViewModel(findIdIView) as T
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}