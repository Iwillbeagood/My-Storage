package com.example.mystorage

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.databinding.ActivityMainBinding
import com.example.mystorage.retrofit.response.ApiResponse
import com.example.mystorage.mvvm.user.view.changePassword.ChangePage
import com.example.mystorage.mvvm.user.view.findId.FindIdPage
import com.example.mystorage.mvvm.user.view.signIn.SignInPage
import com.example.mystorage.mvvm.user.view.login.LoginIView
import com.example.mystorage.mvvm.user.viewmodel.login.LoginViewModel
import com.example.mystorage.mvvm.user.viewmodel.login.LoginViewModelFactory
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.ActivityUtil.goToNextActivity
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants.TAG
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

        // set status bar to transparent
        fun Activity.setStatusBarTransparent() {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        checkAutoLogin()
    }

    override fun onLoginSuccess(message: String?, id: String, password: String) {
        Log.d(TAG, "MainActivity - onLoginSuccess() called")
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()

        App.prefs.setString("userid", id)
        App.prefs.setString("userpassword", password)

        getResponseOnUserHomeInfoCheck()
    }

    override fun onLoginError(message: String?) {
        Log.d(TAG, "MainActivity - onLoginError() called")
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    fun toast(message: String?) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.passwordChangeView -> {
                goToNextActivity(this, ChangePage())
            }
            R.id.findIdView -> {
                goToNextActivity(this, FindIdPage())
            }
            R.id.signInView -> {
                goToNextActivity(this, SignInPage())
            }
            R.id.LoginBtn -> {
                Log.d(TAG, "MainActivity - loginBtn onClick() called")
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                binding.viewModel!!.onLogin()
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
                        toast("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                toast("통신 실패")
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
                goToNextActivity(this, UserInformActivity())
            }
        }
    }
}