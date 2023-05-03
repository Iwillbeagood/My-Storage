package com.example.mystorage.mvvm.item.view.autoItemAdd

import android.graphics.Bitmap
import android.net.Uri

interface AutoItemAddIView {
    fun addReceiptBitmap(receiptBitmap: Bitmap)
    fun addReceiptURL(receiptUri: Uri)
    fun onItemAddSuccess(message: String?)
    fun onItemAddError(message: String?)
}