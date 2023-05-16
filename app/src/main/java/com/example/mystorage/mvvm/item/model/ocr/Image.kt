package com.example.mystorage.mvvm.item.model.ocr

data class Image(
    val fields: List<Field>,
    val inferResult: String,
    val message: String,
    val name: String,
    val uid: String,
    val validationResult: ValidationResult
)