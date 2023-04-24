package com.example.mystorage.mvvm.user.viewmodel.changePassword

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mystorage.retrofit.response.ApiResponse
import com.example.mystorage.mvvm.user.model.user.User
import com.example.mystorage.mvvm.user.view.changePassword.ChangeIView
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.TextWatcherUtil
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeViewModel(private val changeIView: ChangeIView): ViewModel(), ChangeIViewModel {
    private val user: User = User("", "", "", "", "", "", "")

    val idTextWatcher: TextWatcher = TextWatcherUtil.createTextWatcher(user::setID)
    val passwordTextWatcher: TextWatcher = TextWatcherUtil.createTextWatcher(user::setPassword)
    val passwordCheckTextWatcher: TextWatcher = TextWatcherUtil.createTextWatcher(user::setPasswordCheck)
    val authenticationTextWatcher: TextWatcher = TextWatcherUtil.createTextWatcher(user::setTypedAuthenticationNum)

    val phoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            user.setPhone(s.toString())
            changeIView.phoneTextChange()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onChange() {
        if (!user.changeIsValid().first) {
            changeIView.onChangeError(user.changeIsValid().second)
        } else {
            Log.d(TAG, "ChangeViewModel - 입력 오류 없음")
            getResponseOnChange()
        }
    }

    override fun setAuthenticationNum(authenticationNum: String) {
        user.setAuthenticationNum(authenticationNum)
    }

    override fun getResponseOnChange() {
        val api = RetrofitManager.getChangeApiService()

        val call = api.changeUserPassword(
            user.getID().toString(),
            user.getPassword().toString(),
            user.getPhone().toString()
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    if (response.isSuccessful) {
                        try {
                            userRegistrationResponse(response.body()!!)
                        } catch (e: JSONException) {
                            changeIView.onChangeError("응답 결과 파싱 중 오류가 발생했습니다.")
                        }
                    } else {
                        Log.d(TAG, "ChangeViewModel - onResponse() called is not Successful")
                        changeIView.onChangeError(response.body()!!.message)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                changeIView.onChangeError("통신 실패")
                call.cancel()
            }
        })
    }

    override fun userRegistrationResponse(response: ApiResponse) {
        val message = response.message

        if (response.status == "true") {
            Log.d(TAG, "ChangeViewModel - onSuccess() called - response: $message")
            changeIView.onChangeSuccess(message)
        }
        else {
            Log.d(TAG, "ChangeViewModel - onError() called - response: $message")
            changeIView.onChangeError(message)
        }

    }

}