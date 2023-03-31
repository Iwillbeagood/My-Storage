package com.example.mystorage.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.R
import com.example.mystorage.databinding.ActivitySignInPageBinding
import com.example.mystorage.login.view.IView
import com.example.mystorage.login.viewmodel.SignInViewModel
import com.example.mystorage.login.viewmodel.ViewModelFactory
import com.example.mystorage.utils.Constants.TAG

class SignInPage : AppCompatActivity(), IView {
    private lateinit var binding: ActivitySignInPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in_page)
        val signInViewModelFactory = ViewModelFactory(this)
        binding.viewModel = ViewModelProvider(this, signInViewModelFactory)[SignInViewModel::class.java]

    }

    override fun onSignInSuccess(message: String?) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

    override fun onSignInError(message: String?) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }
}