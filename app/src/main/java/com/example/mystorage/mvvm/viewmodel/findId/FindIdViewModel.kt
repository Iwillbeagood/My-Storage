package com.example.mystorage.mvvm.viewmodel.findId

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mystorage.mvvm.model.UserRegistrationResponse
import com.example.mystorage.mvvm.model.user.User
import com.example.mystorage.mvvm.view.findId.FindIdIView
import com.example.mystorage.retrofit.retrofitManager.UserRetrofitManager
import com.example.mystorage.utils.Constants
import com.example.mystorage.utils.TextWatcherUtil.createTextWatcher
import com.example.mystorage.utils.UserResponseManager
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindIdViewModel(private val findIdIView: FindIdIView): ViewModel(), FindIdIViewModel {
    private val user: User = User("", "", "", "", "", "", "")

    val phoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            user.setPhone(s.toString())
            findIdIView.phoneTextChange()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
    val authenticationTextWatcher: TextWatcher = createTextWatcher(user::setTypedAuthenticationNum)


    override fun onFindId() {
        if (!user.findIdIsValid().first) {
            findIdIView.onFindIdError(user.findIdIsValid().second)
        } else {
            Log.d(Constants.TAG, "입력 오류 없음")
            getResponseOnFindId()
        }
    }

    override fun setAuthenticationNum(authenticationNum: String) {
        user.setAuthenticationNum(authenticationNum)
    }

    override fun getResponseOnFindId() {
        val api = UserRetrofitManager.getFindIdApiService()

        val call = api.findIdUser(
            user.getPhone().toString()
        )

        call.enqueue(object : Callback<UserRegistrationResponse> {
            override fun onResponse(
                call: Call<UserRegistrationResponse>,
                response: Response<UserRegistrationResponse>
            ) {
                if (response.body() != null) {
                    try {
                        userRegistrationResponse(response.body()!!)
                    } catch (e: JSONException) {
                        findIdIView.onFindIdError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }

            override fun onFailure(call: Call<UserRegistrationResponse>, t: Throwable) {
                findIdIView.onFindIdError("통신 실패")
                call.cancel()
            }
        })
    }

    override fun userRegistrationResponse(response: UserRegistrationResponse) {
        val userResponseManager = UserResponseManager.getInstance()

        userResponseManager.userRegistrationResponse(response,
            object : UserResponseManager.OnResponseCompleteListener {
                override fun onSuccess(message: String) {
                    Log.d(Constants.TAG, "FindViewModel - onResponse() called - response: $message")
                    findIdIView.onFindIdSuccess(message)
                }

                override fun onError(message: String) {
                    Log.d(Constants.TAG, "FindViewModel - onResponse() called - response: $message")
                    findIdIView.onFindIdSuccess(message)
                }
            })
    }
}