package com.example.mystorage.utils

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mystorage.R
import com.example.mystorage.databinding.CustomToastBinding

object CustomToast {
    fun createToast(context: Context, message: String): Toast {
        val inflater = LayoutInflater.from(context)
        val binding: CustomToastBinding =
            DataBindingUtil.inflate(inflater, R.layout.custom_toast, null, false)

        binding.toastText.text = message

        return Toast(context).apply {
            setGravity(Gravity.CENTER_VERTICAL, 0, 64.toPx())
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}
