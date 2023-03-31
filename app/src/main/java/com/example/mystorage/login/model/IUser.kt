package com.example.mystorage.login.model

interface IUser {
    fun getName(): String?
    fun setName(name: String)
    fun getID(): String?
    fun setID(email: String)
    fun getPassword(): String?
    fun setPassword(password: String)
    fun getPasswordCheck(): String?
    fun setPasswordCheck(passwordCheck: String)
    fun getPhone(): String?
    fun setPhone(phone: String)
    fun isValid(): Int
}