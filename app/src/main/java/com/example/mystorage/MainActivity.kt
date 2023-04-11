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
import com.example.mystorage.mvvm.view.changePassword.ChangePage
import com.example.mystorage.mvvm.view.findId.FindIdPage
import com.example.mystorage.mvvm.view.signIn.SignInPage
import com.example.mystorage.mvvm.view.login.LoginIView
import com.example.mystorage.mvvm.viewmodel.login.LoginViewModel
import com.example.mystorage.mvvm.viewmodel.login.LoginViewModelFactory
import com.example.mystorage.utils.ActivityUtil
import com.example.mystorage.utils.ActivityUtil.goToNextActivity
import com.example.mystorage.utils.Constants.TAG

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
    }

    override fun onLoginSuccess(message: String?) {
        Log.d(TAG, "MainActivity - onLoginSuccess() called")
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
        ActivityUtil.goToNextActivity(this, MainPage())
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
}