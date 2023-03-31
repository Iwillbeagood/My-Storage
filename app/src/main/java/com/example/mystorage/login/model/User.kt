package com.example.mystorage.login.model

import android.text.TextUtils
import androidx.databinding.BaseObservable

class User(
    private var name: String?,
    private var id: String?,
    private var password: String?,
    private var passwordCheck: String?,
    private var phone: String?
) : IUser, BaseObservable() {
    override fun getName(): String? {
        return name
    }

    override fun setName(name: String) {
        this.name = name
    }

    override fun getID(): String? {
        return id
    }

    override fun setID(userid: String) {
        this.id = userid
    }

    override fun getPassword(): String? {
        return password
    }

    override fun setPassword(password: String) {
        this.password = password
    }

    override fun getPasswordCheck(): String? {
        return passwordCheck
    }

    override fun setPasswordCheck(passwordCheck: String) {
        this.passwordCheck = passwordCheck
    }

    override fun getPhone(): String? {
        return phone
    }

    override fun setPhone(phone: String) {
        this.phone = phone
    }

    override fun isValid(): Int {
        return if (TextUtils.isEmpty(getName()))
            0
        else if (TextUtils.isEmpty(getID()))
            1
        else if (TextUtils.isEmpty(getPassword()))
            2
        else if (getPassword()?.length!! <=6)
            3
        else if (TextUtils.isEmpty(getPasswordCheck()))
            4
        else if (getPassword() != getPasswordCheck())
            5
        else if (TextUtils.isEmpty(getPhone()))
            6
        else if (getPhone()?.length!! == 11)
            7
        else
            -1;
    }
}