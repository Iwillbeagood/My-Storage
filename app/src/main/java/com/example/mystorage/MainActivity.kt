package com.example.mystorage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.databinding.ActivityMainBinding
import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.mvvm.user.view.changePassword.ChangePage
import com.example.mystorage.mvvm.user.view.findId.FindIdPage
import com.example.mystorage.mvvm.user.view.signIn.SignInPage
import com.example.mystorage.mvvm.user.view.login.LoginIView
import com.example.mystorage.mvvm.user.viewmodel.login.LoginViewModel
import com.example.mystorage.mvvm.user.viewmodel.login.LoginViewModelFactory
import com.example.mystorage.mvvm.userHome.view.UserHomeActivity
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.ActivityUtil.goToNextActivity
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.CustomToast
import com.example.mystorage.utils.FocusChangeListener
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), LoginIView, View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity - onCreate() called")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val loginViewModelFactory = LoginViewModelFactory(this)
        binding.viewModel = ViewModelProvider(this, loginViewModelFactory)[LoginViewModel::class.java]

        binding.passwordChangeView.setOnClickListener(this)
        binding.findIdView.setOnClickListener(this)
        binding.signInView.setOnClickListener(this)
        binding.LoginBtn.setOnClickListener(this)

        FocusChangeListener.setEditTextFocusChangeListener(binding.idEdit, binding.idLayout)
        FocusChangeListener.setEditTextFocusChangeListener(binding.passwordEdit, binding.passwordLayout)

        checkAutoLogin()
    }

    override fun onLoginSuccess(message: String?, id: String, password: String) {
        Log.d(TAG, "MainActivity - onLoginSuccess() called")
        CustomToast.createToast(this, message.toString()).show()

        App.prefs.setString("userid", id)
        App.prefs.setString("userpassword", password)

        getResponseOnUserHomeInfoCheck()
    }

    override fun onLoginError(message: String?) {
        Log.d(TAG, "MainActivity - onLoginError() called")
        CustomToast.createToast(this, message.toString()).show()
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.passwordChangeView -> {
                if (binding.passwordChangeView.isEnabled) {
                    binding.passwordChangeView.isEnabled = false
                    goToNextActivity(this, ChangePage())
                    binding.passwordChangeView.isEnabled = true
                }
            }
            R.id.findIdView -> {
                if (binding.findIdView.isEnabled) {
                    binding.findIdView.isEnabled = false
                    goToNextActivity(this, FindIdPage())
                    binding.findIdView.isEnabled = true
                }
            }
            R.id.signInView -> {
                if (binding.signInView.isEnabled) {
                    binding.signInView.isEnabled = false
                    goToNextActivity(this, SignInPage())
                    binding.signInView.isEnabled = true
                }
            }
            R.id.LoginBtn -> {
                if (binding.LoginBtn.isEnabled) {
                    binding.LoginBtn.isEnabled = false
                    Log.d(TAG, "MainActivity - loginBtn onClick() called")
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    binding.viewModel!!.onLogin()
                    binding.LoginBtn.isEnabled = true
                }
            }
        }
    }

    override fun checkAutoLogin() {
        val savedUserId = App.prefs.getString("userid", "")
        val savedUserPassword = App.prefs.getString("userpassword", "")

        if (savedUserId.isNotEmpty() && savedUserPassword.isNotEmpty()) {
            binding.viewModel!!.setAutoLogin(savedUserId, savedUserPassword)
            binding.viewModel!!.onLogin()
        }
    }

    override fun getResponseOnUserHomeInfoCheck() {
        Log.d(TAG, "MainActivity - checkUserHomeInfo() called")
        val api = RetrofitManager.getUserInfoHomeCheckApiService()

        val call = api.userHomeInfoCheck(
            App.prefs.getString("userid", "")
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        userHomeInfoCheckResponse(response.body()!!)
                    } catch (e: JSONException) {
                        onLoginError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                onLoginError("통신 실패")
                call.cancel()
            }
        })
    }

    override fun userHomeInfoCheckResponse(response: ApiResponse) {
        val message = response.message
        when (response.status) {
            "true" -> {
                // 초반 설정을 완료했음으로 바로 메인 페이지로 이동
                goToNextActivity(this, MainPage())
            }
            "false" -> {
                // 초반 설정을 완료하지 못했음으로 바로 설정 페이지로 이동
                goToNextActivity(this, UserHomeActivity())
            }
        }
    }
}