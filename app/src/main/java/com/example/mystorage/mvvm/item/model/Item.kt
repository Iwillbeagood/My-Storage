package com.example.mystorage.mvvm.item.model

import android.text.TextUtils
import androidx.databinding.BaseObservable

class Item(
    private var itemname: String?,
    private var itemimage: String?,
    private var itemplace: String?,
    private var itemstore: String?,
    private var itemcount: String?
) : IItem, BaseObservable() {
    override fun getItemname(): String? {
        return itemname
    }

    override fun setItemname(itemname: String) {
        this.itemname = itemname
    }

    override fun getItemimage(): String? {
        return itemimage
    }

    override fun setItemimage(itemimage: String) {
        this.itemimage = itemimage
    }

    override fun getItemplace(): String? {
        return itemplace
    }

    override fun setItemplace(itemplace: String) {
        this.itemplace = itemplace
    }

    override fun getItemstore(): String? {
        return itemstore
    }

    override fun setItemstore(itemstore: String) {
        this.itemstore = itemstore
    }

    override fun getItemcount(): String? {
        return itemcount
    }

    override fun setItemcount(itemcount: String) {
        this.itemcount = itemcount
    }

    override fun itemIsValid(): Pair<Boolean, String> {
        return if (TextUtils.isEmpty(getItemname()))
            Pair(false, "물건 이름은 필수 입니다!")
        else
            Pair(true, "")
    }
}