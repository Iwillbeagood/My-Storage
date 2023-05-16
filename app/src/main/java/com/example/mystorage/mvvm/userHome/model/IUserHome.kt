package com.example.mystorage.mvvm.userHome.model

interface IUserHome {
    fun getLivingroom(): String?
    fun setLivingroom(livingroom: String?)
    fun getKitchen(): String?
    fun setKitchen(kitchen: String?)
    fun getStorage(): String?
    fun setStorage(storage: String?)
    fun getRoomNames(): String?
    fun setRoomNames(roomNames: String?)
    fun getBathroomNames(): String?
    fun setBathroomNames(bathroomNames: String?)
    fun getEtcName(): String?
    fun setEtcName(etcName: String?)
}