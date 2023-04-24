package com.example.mystorage.mvvm.userhome.model

interface IUserHome {
    fun getLivingRoomName(): String?
    fun setLivingRoomName(livingRoomName: String?)
    fun getKitchenName(): String?
    fun setKitchenName(kitchenName: String?)
    fun getStorageName(): String?
    fun setStorageName(storageName: String?)
    fun getRoomNames(): String?
    fun setRoomNames(roomNames: String?)
    fun getBathroomNames(): String?
    fun setBathroomNames(bathroomNames: String?)
    fun getEtcName(): String?
    fun setEtcName(etcName: String?)
}