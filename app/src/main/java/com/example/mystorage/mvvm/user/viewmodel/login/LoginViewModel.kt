package com.example.mystorage.mvvm.user.viewmodel.login

import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mystorage.mvvm.user.model.user.User
import com.example.mystorage.retrofit.response.ApiResponse
import com.example.mystorage.mvvm.user.view.login.LoginIView
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.TextWatcherUtil.createTextWatcher
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val loginIView: LoginIView): ViewModel(), LoginViewIModel {
    private val user: User = User("", "", "", "", "", "", "")

    val idTextWatcher: TextWatcher = createTextWatcher(user::setID)
    val passwordTextWatcher: TextWatcher = createTextWatcher(user::setPassword)

    override fun onLogin() {
        if (!user.loginIsValid().first) {
            loginIView.onLoginError(user.signInIsValid().second)
        }
        else {
            Log.d(TAG, "LoginViewModel - 입력 오류 없음")
            getResponseOnLogin()
        }
    }

    override fun getResponseOnLogin() {
        val api = RetrofitManager.getLoginApiService()

        val call = api.loginUser(
            user.getID().toString(),
            user.getPassword().toString())

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        userRegistrationResponse(response.body()!!)
                    } catch (e: JSONException) {
                        loginIView.onLoginError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                loginIView.onLoginError("통신 실패")
                call.cancel()
            }
        })
    }

    override fun userRegistrationResponse(response: ApiResponse) {
        val message = response.message

        if (response.status == "true") {
            Log.d(TAG, "LoginViewModel - onSuccess() called - response: $message")
            loginIView.onLoginSuccess(message, user.getID().toString(), user.getPassword().toString())
        }
        else {
            Log.d(TAG, "LoginViewModel - onError() called - response: $message")
            loginIView.onLoginError(message)
        }
    }

    override fun setAutoLogin(id: String, password: String) {
        user.setID(id)
        user.setPassword(password)
    }
}