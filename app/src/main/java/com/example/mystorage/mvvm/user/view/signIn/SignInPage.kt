package com.example.mystorage.mvvm.user.view.signIn

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.MainActivity
import com.example.mystorage.R
import com.example.mystorage.databinding.ActivitySignInPageBinding
import com.example.mystorage.mvvm.user.viewmodel.signIn.SignInViewModel
import com.example.mystorage.mvvm.user.viewmodel.signIn.SignInViewModelFactory
import com.example.mystorage.utils.ActivityUtil.goToNextActivity
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.CustomToast
import com.example.mystorage.utils.FocusChangeListener

import com.example.mystorage.retrofit.retrofitManager.NaverSMSManager
import java.util.*


class SignInPage : AppCompatActivity(), SignInIView, View.OnClickListener {
    private lateinit var binding: ActivitySignInPageBinding
    private var phoneTextChanged = false
    private val mainActivity = MainActivity()
    private val random = Random()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "SignInPage - onCreate() called")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in_page)
        val signInViewModelFactory = SignInViewModelFactory(this)
        binding.viewModel = ViewModelProvider(this, signInViewModelFactory)[SignInViewModel::class.java]

        binding.authenticationBtnInSignIn.setOnClickListener(this)
        binding.signIntBtn.setOnClickListener(this)
        binding.signInBack.setOnClickListener(this)

        FocusChangeListener.setEditTextFocusChangeListener(binding.nickNameEdit, binding.nickNameLayout)
        FocusChangeListener.setEditTextFocusChangeListener(binding.idEditInSignIn, binding.idLayoutInSignIn)
        FocusChangeListener.setEditTextFocusChangeListener(binding.passwordEditInSignIn, binding.passwordLayoutInSignIn)
        FocusChangeListener.setEditTextFocusChangeListener(binding.passwordCheckEditInSignIn, binding.passwordCheckLayoutInSignIn)
        FocusChangeListener.setEditTextFocusChangeListener(binding.phoneEditInSignIn, binding.phoneLayoutInSignIn)
        FocusChangeListener.setEditTextFocusChangeListener(binding.authenticationEditInSignIn, binding.authenticationLayoutInSignIn)
    }

    override fun onSignInSuccess(message: String?) {
        Log.d(TAG, "SignInPage - onSignInSuccess() called")
        CustomToast.createToast(this, message.toString()).show()
        goToNextActivity(this, mainActivity)
    }

    override fun onSignInError(message: String?) {
        Log.d(TAG, "SignInPage - onSignInError() called")
        CustomToast.createToast(this, message.toString()).show()
    }

    // 전화번호의 Text 를 바꿨으면 무조건 인증번호를 다시 받아야 함
    override fun phoneTextChange() {
        phoneTextChanged = false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.authenticationBtnInSignIn -> {
                if (binding.authenticationBtnInSignIn.isEnabled) {
                    binding.authenticationBtnInSignIn.isEnabled = false
                    Log.d(TAG, "SignInPage - authenticationBtnInSignIn onClick() called")
                    phoneTextChanged = true
                    sendSMS()
                    binding.authenticationBtnInSignIn.isEnabled = true
                }
            }
            R.id.signIntBtn -> {
                if (binding.signIntBtn.isEnabled) {
                    binding.signIntBtn.isEnabled = false
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    if (phoneTextChanged || binding.phoneEditInSignIn.text.isNullOrBlank()) {
                        Log.d(TAG, "SignInPage - signIntBtn onClick() called")
                        binding.viewModel!!.onSignIn()
                    } else {
                        CustomToast.createToast(this, "전화번호가 변경되었습니다. 인증번호를 다시 입력해주세요.").show()
                    }
                    binding.signIntBtn.isEnabled = true
                }
            }
            R.id.signInBack -> {
                if (binding.signInBack.isEnabled) {
                    binding.signInBack.isEnabled = false
                    Log.d(TAG, "SignInPage - signInBack onClick() called")
                    goToNextActivity(this, mainActivity)
                    binding.signInBack.isEnabled = true
                }
            }
        }
    }

    override fun sendSMS() {
        val authenticationNum = random.nextInt(900000) + 100000
        val naverSMSManager = NaverSMSManager.getInstance(this)

        naverSMSManager.sendSMS(
            binding.phoneEditInSignIn.text.toString(), authenticationNum,
            object : NaverSMSManager.OnSMSCompleteListener {
                override fun onSuccess() {
                    binding.viewModel!!.setAuthenticationNum(authenticationNum.toString())
                }

                override fun onError(message: String) {
                    onSignInError(message)
                }
            })
    }
}