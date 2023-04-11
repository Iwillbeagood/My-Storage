package com.example.mystorage.mvvm.view.findId

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
import com.example.mystorage.databinding.ActivityFindIdPageBinding
import com.example.mystorage.mvvm.viewmodel.findId.FindIdViewModel
import com.example.mystorage.mvvm.viewmodel.findId.FindIdViewModelFactory
import com.example.mystorage.utils.ActivityUtil
import com.example.mystorage.utils.ActivityUtil.goToNextActivity
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.NaverSMSManager
import java.util.*

class FindIdPage : AppCompatActivity(), FindIdIView, View.OnClickListener {
    private lateinit var binding: ActivityFindIdPageBinding
    private var phoneTextChanged = false
    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "FindIdPage - onCreate() called")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_id_page)
        val findIdViewModelFactory = FindIdViewModelFactory(this)
        binding.viewModel = ViewModelProvider(this, findIdViewModelFactory)[FindIdViewModel::class.java]

        binding.authenticationBtnInFindId.setOnClickListener(this)
        binding.findIdBtn.setOnClickListener(this)
        binding.findIdBack.setOnClickListener(this)
    }

    override fun onFindIdSuccess(message: String?) {
        Log.d(TAG, "FindIdPage - onFindIdSuccess() called")
        binding.findIdResultText.text = message
        binding.findIdResult.visibility = View.VISIBLE
    }

    override fun onFindIdError(message: String?) {
        Log.d(TAG, "FindIdPage - onFindIdError() called")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun phoneTextChange() {
        phoneTextChanged = false
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.authenticationBtnInFindId -> {
                phoneTextChanged = true
                sendSMS()
            }
            R.id.findIdBtn -> {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                if (phoneTextChanged) {
                    Log.d(TAG, "FindIdPage - findIdBtn onClick() called")
                    binding.viewModel!!.onFindId()
                } else {
                    Toast.makeText(this, "전화번호가 변경되었습니다. 인증번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.findIdBack -> {
                Log.d(TAG, "FindIdPage - findIdBack onClick() called")
                goToNextActivity(this, MainActivity())
            }
        }
    }

    private fun sendSMS() {
        val authenticationNum = random.nextInt(900000) + 100000
        val naverSMSManager = NaverSMSManager.getInstance(this)
        naverSMSManager.sendSMS(
            binding.phoneEditInFindId.text.toString(), authenticationNum,
            object : NaverSMSManager.OnSMSCompleteListener {
                override fun onSuccess() {
                    binding.viewModel!!.setAuthenticationNum(authenticationNum.toString())
                }

                override fun onError(message: String) {
                    onFindIdError(message)
                }
            })
    }
}