package com.example.mystorage.mvvm.userHome.model

import androidx.databinding.BaseObservable

class UserHome(
    private var livingroom: String?,
    private var kitchen: String?,
    private var storage: String?,
    private var roomNames: String?,
    private var bathroomNames: String?,
    private var etcName: String?
) : IUserHome, BaseObservable() {
    override fun getLivingroom(): String? {
        return livingroom
    }

    override fun setLivingroom(livingroom: String?) {
        this.livingroom = livingroom
    }

    override fun getKitchen(): String? {
        return kitchen
    }

    override fun setKitchen(kitchen: String?) {
        this.kitchen = kitchen
    }

    override fun getStorage(): String? {
        return storage
    }

    override fun setStorage(storage: String?) {
        this.storage = storage
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