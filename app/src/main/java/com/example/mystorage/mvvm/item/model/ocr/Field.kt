package com.example.mystorage.mvvm.item.model.ocr

data class Field(
    val boundingPoly: BoundingPoly,
    val inferConfidence: Double,
    val inferText: String,
    val lineBreak: Boolean,
    val type: String,
    val valueType: String
)