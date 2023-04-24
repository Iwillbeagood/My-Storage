package com.example.mystorage.mvvm.item.model

interface IItem {
    fun getItemname(): String?
    fun setItemname(itemname: String)
    fun getItemimage(): String?
    fun setItemimage(itemimage: String)
    fun getItemplace(): String?
    fun setItemplace(itemplace: String)
    fun getItemstore(): String?
    fun setItemstore(itemstore: String)
    fun getItemcount(): String?
    fun setItemcount(itemcount: String)
    fun itemIsValid(): Pair<Boolean, String>
}