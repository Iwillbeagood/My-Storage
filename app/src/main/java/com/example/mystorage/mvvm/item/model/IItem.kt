package com.example.mystorage.mvvm.item.model

interface IItem {
    fun getItemname(): String?
    fun setItemname(itemname: String)
    fun getItemimage(): Pair<String, String>?
    fun setItemimage(itemimage: Pair<String, String>?)
    fun getItemplace(): String?
    fun setItemplace(itemplace: String)
    fun getItemstore(): String?
    fun setItemstore(itemstore: String)
    fun getItemcount(): String?
    fun setItemcount(itemcount: String)
    fun getOriginname(): String?
    fun setOriginname(originname: String)
    fun getOriginimage(): String?
    fun setOriginimage(originimage: String)
    fun itemIsValid(): Pair<Boolean, String>
}