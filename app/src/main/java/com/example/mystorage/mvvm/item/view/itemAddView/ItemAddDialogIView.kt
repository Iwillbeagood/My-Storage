package com.example.mystorage.mvvm.item.view.itemAddView

import android.graphics.Bitmap
import android.net.Uri
import com.example.mystorage.retrofit.response.UserHomeInfoResponse

interface ItemAddDialogIView {
    fun updateImageBitmap(imageBitmap: Bitmap)
    fun updateImageURL(imageUri: Uri)
    fun onItemAddSuccess(message: String?)
    fun onItemAddError(message: String?)
    fun onItemCountUpdate(message: String?)
    fun userHomeInfoLoadResponse(response: UserHomeInfoResponse): MutableList<String>
}