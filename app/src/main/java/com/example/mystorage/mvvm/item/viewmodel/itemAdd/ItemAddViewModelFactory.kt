package com.example.mystorage.mvvm.item.viewmodel.itemAdd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.mvvm.item.view.itemAdd.ItemAddDialogIView

class ItemAddViewModelFactory(private val itemAddDialogIView: ItemAddDialogIView): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemAddViewModel::class.java))
            return ItemAddViewModel(itemAddDialogIView) as T
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}