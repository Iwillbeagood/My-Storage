package com.example.mystorage.mvvm.item.view.itemUpdate

import android.graphics.Bitmap
import android.net.Uri
import com.example.mystorage.retrofit.response.UserHomeInfoResponse
import com.example.mystorage.retrofit.response.UserItem

interface ItemUpdateIView {
    fun updateImageBitmap(imageBitmap: Bitmap)
    fun updateImageURL(imageUri: Uri)
    fun onItemUpdateSuccess(message: String?)
    fun onItemUpdateError(message: String?)
    fun setUpdateSetting(userItem: UserItem)
}