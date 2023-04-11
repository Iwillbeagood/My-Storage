package com.example.mystorage.utils

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.example.mystorage.R

object CustomToast {
    private var toast: Toast? = null

//    fun show(context: Context?, message: String, duration: Int) {
//        if (context == null) return
//
//        if (toast != null) {
//            toast?.cancel()
//        }
//
//        val inflater = LayoutInflater.from(context)
//        val layout = inflater.inflate(R.layout.custom_toast, null)
//        layout.findViewById<TextView>(R.id.toast_text).text = message
//
//        toast = Toast(context)
//        toast?.setGravity(Gravity.BOTTOM, 0, 200)
//        toast?.duration = duration
//        toast?.view = layout
//        toast?.show()
//    }

}