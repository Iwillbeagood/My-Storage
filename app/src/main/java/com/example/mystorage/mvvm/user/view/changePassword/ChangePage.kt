package com.example.mystorage.mvvm.user.view.changePassword

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.MainActivity
import com.example.mystorage.R
import com.example.mystorage.databinding.ActivityChangePasswordPageBinding
import com.example.mystorage.mvvm.user.viewmodel.changePassword.ChangeViewModel
import com.example.mystorage.mvvm.user.viewmodel.changePassword.ChangeViewModelFactory
import com.example.mystorage.mvvm.user.viewmodel.findId.FindIdViewModelFactory
import com.example.mystorage.utils.ActivityUtil.goToNextActivity
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.FocusChangeListener
import com.example.mystorage.utils.NaverSMSManager
import java.util.*

class ChangePage : AppCompatActivity(), ChangeIView, View.OnClickListener {
    private lateinit var binding: ActivityChangePasswordPageBinding
    private var phoneTextChanged = false
    private val random = Random()
    private val mainActivity = MainActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password_page)
        val changeViewModelFactory = ChangeViewModelFactory(this)
        binding.viewModel = ViewModelProvider(this, changeViewModelFactory)[ChangeViewModel::class.java]

        binding.authenticationBtnInChange.setOnClickListener(this)
        binding.changePasswordBtn.setOnClickListener(this)
        binding.changeBack.setOnClickListener(this)

        FocusChangeListener.setEditTextFocusChangeListener(binding.idEditInChange, binding.idLayoutInChange)
        FocusChangeListener.setEditTextFocusChangeListener(binding.passwordChangeEdit, binding.passwordLayoutInChange)
        FocusChangeListener.setEditTextFocusChangeListener(binding.passwordCheckEdit, binding.passwordLayoutInChange)
        FocusChangeListener.setEditTextFocusChangeListener(binding.phoneEditInChange, binding.phoneLayoutInChange)
        FocusChangeListener.setEditTextFocusChangeListener(binding.authenticationEditInChange, binding.authenticationLayoutInChange)


    }

    override fun onChangeSuccess(message: String?) {
        Log.d(TAG, "ChangePage - onChangeSuccess() called")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        goToNextActivity(this, mainActivity)
    }

    override fun onChangeError(message: String?) {
        Log.d(TAG, "ChangePage - onChangeError() called")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun phoneTextChange() {
        phoneTextChanged = false
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.authenticationBtnInChange -> {
                phoneTextChanged = true
                sendSMS()
            }
            R.id.changePasswordBtn -> {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                if (phoneTextChanged || binding.phoneEditInChange.text.isNullOrBlank()) {
                    Log.d(TAG, "ChangePage - changePasswordBtn onClick() called")
                    binding.viewModel!!.onChange()
                } else {
                    Toast.makeText(this, "전화번호가 변경되었습니다. 인증번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.changeBack -> {
                Log.d(TAG, "ChangePage - changeBack onClick() called")
                goToNextActivity(this, mainActivity)
            }
        }
    }

    private fun sendSMS() {
        val authenticationNum = random.nextInt(900000) + 100000
        val naverSMSManager = NaverSMSManager.getInstance(this)

        naverSMSManager.sendSMS(
            binding.phoneEditInChange.text.toString(), authenticationNum,
            object : NaverSMSManager.OnSMSCompleteListener {
                override fun onSuccess() {
                    binding.viewModel!!.setAuthenticationNum(authenticationNum.toString())
                }

                override fun onError(message: String) {
                    onChangeError(message)
                }
            })
    }
}