package com.example.mystorage.mvvm.item.model.ocr

data class OcrRequest(
    val version: String,
    val requestId: String,
    val timestamp: Int,
    val lang: String,
    val images: List<ImageX>
)