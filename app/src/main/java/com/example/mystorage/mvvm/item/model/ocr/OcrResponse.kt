package com.example.mystorage.mvvm.item.model.ocr

data class OcrResponse(
    val images: List<Image>,
    val requestId: String,
    val timestamp: Long,
    val version: String
)