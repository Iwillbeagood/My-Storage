package com.example.mystorage.mvvm.item.viewmodel.itemUpdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.mvvm.item.view.itemUpdate.ItemUpdateIView

class ItemUpdateViewModelFactory(private val itemUpdateIView: ItemUpdateIView): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemUpdateViewModel::class.java))
            return ItemUpdateViewModel(itemUpdateIView) as T
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}