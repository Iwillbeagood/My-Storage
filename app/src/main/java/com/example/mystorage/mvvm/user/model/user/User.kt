package com.example.mystorage.mvvm.user.model.user

import android.text.TextUtils
import androidx.databinding.BaseObservable
import com.example.mystorage.mvvm.user.model.user.IUser

class User(
    private var username: String?,
    private var userid: String?,
    private var userpassword: String?,
    private var userpasswordcheck: String?,
    private var userphone: String?,
    private var authenticationNum: String?,
    private var typedAuthenticationNum: String?
) : IUser, BaseObservable() {
    override fun getName(): String? {
        return username
    }

    override fun setName(name: String) {
        this.username = name
    }

    override fun getID(): String? {
        return userid
    }

    override fun setID(id: String) {
        this.userid = id
    }

    override fun getPassword(): String? {
        return userpassword
    }

    override fun setPassword(password: String) {
        this.userpassword = password
    }

    override fun getPasswordCheck(): String? {
        return userpasswordcheck
    }

    override fun setPasswordCheck(passwordCheck: String) {
        this.userpasswordcheck = passwordCheck
    }

    override fun getPhone(): String? {
        return userphone
    }

    override fun setPhone(phone: String) {
        this.userphone = phone
    }

    override fun getAuthenticationNum(): String? {
        return authenticationNum
    }

    override fun setAuthenticationNum(authenticationNum: String) {
        this.authenticationNum = authenticationNum
    }

    override fun getTypedAuthenticationNum(): String? {
        return typedAuthenticationNum
    }

    override fun setTypedAuthenticationNum(typedAuthenticationNum: String) {
        this.typedAuthenticationNum = typedAuthenticationNum
    }

    override fun signInIsValid(): Pair<Boolean, String> {
        return if (TextUtils.isEmpty(getName()))
            Pair(false, "닉네임을 입력해 주세요")
        else if (TextUtils.isEmpty(getID()))
            Pair(false, "아이디를 입력해 주세요")
        else if (TextUtils.isEmpty(getPassword()))
            Pair(false, "비밀번호를 입력해 주세요")
        else if (getPassword()?.length!! < 6)
            Pair(false, "6자리 이상의 비밀번호를 입력해 주세요")
        else if (TextUtils.isEmpty(getPasswordCheck()))
            Pair(false, "비밀번호 확인을 입력해 주세요")
        else if (getPassword() != getPasswordCheck())
            Pair(false, "비밀번호와 비밀번호 확인이 일치하지 않습니다")
        else if (TextUtils.isEmpty(getPhone()))
            Pair(false, "전화번호를 입력해 주세요")
        else if (getPhone()?.length!! == 12)
            Pair(false, "전화번호 11자리에 맞게 입력해 주세요")
        else if (getTypedAuthenticationNum()?.length!! != 6)
            Pair(false, "인증번호는 6자리 입니다")
        else if (getAuthenticationNum() != getTypedAuthenticationNum())
            Pair(false, "인증번호가 일치하지 않습니다")
        else
            Pair(true, "")
    }

    override fun loginIsValid(): Pair<Boolean, String> {
        return if (TextUtils.isEmpty(getID()))
            Pair(false, "아이디를 입력해 주세요")
        else if (TextUtils.isEmpty(getPassword()))
            Pair(false, "비밀번호를 입력해 주세요")
        else
            Pair(true, "")
    }

    override fun findIdIsValid(): Pair<Boolean, String> {
        return if (TextUtils.isEmpty(getPhone()))
            Pair(false, "전화번호를 입력해 주세요")
        else if (getPhone()?.length!! == 12)
            Pair(false, "전화번호 11자리에 맞게 입력해 주세요")
        else if (getTypedAuthenticationNum()?.length!! != 6)
            Pair(false, "인증번호는 6자리 입니다")
        else if (getAuthenticationNum() != getTypedAuthenticationNum())
            Pair(false, "인증번호가 일치하지 않습니다")
        else
            Pair(true, "")
    }

    override fun changeIsValid(): Pair<Boolean, String> {
        return if (TextUtils.isEmpty(getID()))
            Pair(false, "아이디를 입력해 주세요")
        else if (TextUtils.isEmpty(getPassword()))
            Pair(false, "비밀번호를 입력해 주세요")
        else if (getPassword()?.length!! < 6)
            Pair(false, "6자리 이상의 비밀번호를 입력해 주세요")
        else if (TextUtils.isEmpty(getPasswordCheck()))
            Pair(false, "비밀번호 확인을 입력해 주세요")
        else if (getPassword() != getPasswordCheck())
            Pair(false, "비밀번호와 비밀번호 확인이 일치하지 않습니다")
        else if (TextUtils.isEmpty(getPhone()))
            Pair(false, "전화번호를 입력해 주세요")
        else if (getPhone()?.length!! == 12)
            Pair(false, "전화번호 11자리에 맞게 입력해 주세요")
        else if (getTypedAuthenticationNum()?.length!! != 6)
            Pair(false, "인증번호는 6자리 입니다")
        else if (getAuthenticationNum() != getTypedAuthenticationNum())
            Pair(false, "인증번호가 일치하지 않습니다")
        else
            Pair(true, "")
    }
}