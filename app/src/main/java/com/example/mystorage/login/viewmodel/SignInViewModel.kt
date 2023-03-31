package com.example.mystorage.login.viewmodel

import androidx.lifecycle.ViewModel
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.mystorage.login.model.User
import com.example.mystorage.login.view.IView
import com.example.mystorage.utils.Constants.IP_ADDRESS
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SignInViewModel(
    private val signInView: IView
): ViewModel(), IViewModel {

    private val user: User = User("", "", "", "", "")

    val nameTextWatcher:TextWatcher
        get()= object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                user.setName(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }

    val idTextWatcher: TextWatcher
        get()= object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                user.setID(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }


    val passwordTextWatcher:TextWatcher
        get()= object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                user.setPassword(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }

    val passwordCheckTextWatcher:TextWatcher
        get()= object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                user.setPasswordCheck(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }

    val phoneTextWatcher:TextWatcher
        get()= object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                user.setPhone(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }

    val authenticationTextWatcher:TextWatcher
        get()= object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }


    override fun onSignIn(view: View) {
        when (user.isValid()) {
            0 -> {
                signInView.onSignInError("닉네임을 입력해 주세요")
            }
            1 -> {
                signInView.onSignInError("아이디를 입력해 주세요")
            }
            2 -> {
                signInView.onSignInError("비밀번호를 입력해 주세요")
            }
            3 -> {
                signInView.onSignInError("6자리 이하의 비밀번호를 입력해 주세요")
            }
            4 -> {
                signInView.onSignInError("비밀번호 확인을 입력해 주세요")
            }
            5 -> {
                signInView.onSignInError("비밀번호와 비밀번호 확인이 일치하지 않습니다")
            }
            6 -> {
                signInView.onSignInError("전화번호를 입력해 주세요")
            }
            7 -> {
                signInView.onSignInError("전화번호 11자리에 맞게 입력해 주세요")
            }
            else -> {
                getResponseSignIn()
            }
        }
    }

    private fun getResponseSignIn() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://${IP_ADDRESS}/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(RetrofitSignIn::class.java)
        val call = api.registerUser(user)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                // 서버 응답 처리
                if (response.isSuccessful) {
                    signInView.onSignInSuccess("회원가입 성공")
                    // 로그인 화면으로 보냄
                } else {
                    signInView.onSignInError(response.body().toString())

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 통신 실패 처리
                signInView.onSignInError("통신 실패")
            }
        })
    }
}