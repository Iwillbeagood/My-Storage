package com.example.mystorage.mvvm.viewmodel.signIn

import androidx.lifecycle.ViewModel
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.mystorage.mvvm.model.user.User
import com.example.mystorage.mvvm.model.UserRegistrationResponse
import com.example.mystorage.mvvm.view.signIn.SignInIView
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.retrofit.retrofitManager.UserRetrofitManager
import com.example.mystorage.utils.TextWatcherUtil.createTextWatcher
import com.example.mystorage.utils.UserResponseManager
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInViewModel(private val signInView: SignInIView): ViewModel(), SignInIViewModel {
    private val user: User = User("", "", "", "", "", "", "")

    val nameTextWatcher: TextWatcher = createTextWatcher(user::setName)
    val idTextWatcher: TextWatcher = createTextWatcher(user::setID)
    val passwordTextWatcher: TextWatcher = createTextWatcher(user::setPassword)
    val passwordCheckTextWatcher: TextWatcher = createTextWatcher(user::setPasswordCheck)
    val authenticationTextWatcher: TextWatcher = createTextWatcher(user::setTypedAuthenticationNum)

    val phoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            user.setPhone(s.toString())
            signInView.phoneTextChange()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }


    override fun onSignIn() {
        if (!user.signInIsValid().first) {
            signInView.onSignInError(user.signInIsValid().second)
        } else {
            Log.d(TAG, "SignInViewModel - 입력 오류 없음")
            getResponseOnSignIn()
        }
    }

    override fun setAuthenticationNum(authenticationNum: String) {
        user.setAuthenticationNum(authenticationNum)
    }

    override fun getResponseOnSignIn() {
        val api = UserRetrofitManager.getSignInApiService()

        val call = api.registerUser(
            user.getName().toString(),
            user.getID().toString(),
            user.getPassword().toString(),
            user.getPhone().toString()
        )

        call.enqueue(object : Callback<UserRegistrationResponse> {
            override fun onResponse(call: Call<UserRegistrationResponse>, response: Response<UserRegistrationResponse>) {
                if (response.body() != null) {
                    try {
                        userRegistrationResponse(response.body()!!)
                    } catch (e: JSONException) {
                        signInView.onSignInError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }

            override fun onFailure(call: Call<UserRegistrationResponse>, t: Throwable) {
                signInView.onSignInError("통신 실패")
                call.cancel()
            }
        })
    }

    override fun userRegistrationResponse(response: UserRegistrationResponse) {
        val userResponseManager = UserResponseManager.getInstance()

        userResponseManager.userRegistrationResponse(response,
            object : UserResponseManager.OnResponseCompleteListener {
                override fun onSuccess(message: String) {
                    Log.d(TAG, "SignInViewModel - onResponse() called - response: $message")
                    signInView.onSignInSuccess(message)
                }

                override fun onError(message: String) {
                    Log.d(TAG, "SignInViewModel - onResponse() called - response: $message")
                    signInView.onSignInError(message)
                }
            })
    }
}
