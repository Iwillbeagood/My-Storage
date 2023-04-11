package com.example.mystorage.mvvm.model.user

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
    fun getAuthenticationNum(): String?
    fun setAuthenticationNum(authenticationNum: String)
    fun getTypedAuthenticationNum(): String?
    fun setTypedAuthenticationNum(typedAuthenticationNum: String)
    fun signInIsValid(): Pair<Boolean, String>
    fun loginIsValid() : Pair<Boolean, String>
    fun findIdIsValid() : Pair<Boolean, String>
    fun changeIsValid() : Pair<Boolean, String>
}