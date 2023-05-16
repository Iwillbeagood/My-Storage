package com.example.mystorage.mvvm.item.view.itemAdd

import android.graphics.Bitmap
import android.net.Uri

interface ItemAddDialogIView {
    fun addImageBitmap(imageBitmap: Bitmap)
    fun addImageURL(imageUri: Uri)
    fun onItemAddSuccess(message: String?)
    fun onItemAddError(message: String?)
    fun onItemCountUpdate(itemid: Int, message: String?)
}