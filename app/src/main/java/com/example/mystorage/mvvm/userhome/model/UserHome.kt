package com.example.mystorage.mvvm.userhome.model

import android.text.TextUtils
import androidx.databinding.BaseObservable

class UserHome(
    private var livingRoomName: String?,
    private var kitchenName: String?,
    private var storageName: String?,
    private var roomNames: String?,
    private var bathroomNames: String?,
    private var etcName: String?
) : IUserHome, BaseObservable() {
    override fun getLivingRoomName(): String? {
        return livingRoomName
    }

    override fun setLivingRoomName(livingRoomName: String?) {
        this.livingRoomName = livingRoomName
    }

    override fun getKitchenName(): String? {
        return kitchenName
    }

    override fun setKitchenName(kitchenName: String?) {
        this.kitchenName = kitchenName
    }

    override fun getStorageName(): String? {
        return storageName
    }

    override fun setStorageName(storageName: String?) {
        this.storageName = storageName
    }

    override fun getRoomNames(): String? {
        return roomNames
    }

    override fun setRoomNames(roomNames: String?) {
        this.roomNames = roomNames
    }

    override fun getBathroomNames(): String? {
        return bathroomNames
    }

    override fun setBathroomNames(bathroomNames: String?) {
        this.bathroomNames = bathroomNames
    }

    override fun getEtcName(): String? {
        return etcName
    }

    override fun setEtcName(etcName: String?) {
        this.etcName = etcName
    }

}