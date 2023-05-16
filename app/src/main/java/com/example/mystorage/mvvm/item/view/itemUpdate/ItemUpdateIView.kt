package com.example.mystorage.mvvm.item.view.itemUpdate

import android.graphics.Bitmap
import android.net.Uri

interface ItemUpdateIView {
    fun updateImageBitmap(imageBitmap: Bitmap)
    fun updateImageURL(imageUri: Uri)
    fun onItemUpdateSuccess(message: String?)
    fun onItemUpdateError(message: String?)
    fun setUpdateSetting()
}