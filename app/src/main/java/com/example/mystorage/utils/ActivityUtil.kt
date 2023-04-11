package com.example.mystorage.utils

import android.app.Activity
import android.content.Intent

object ActivityUtil {
    fun goToNextActivity(activity: Activity, nextActivity: Activity) {
        val intent = Intent(activity, nextActivity::class.java)
        activity.startActivity(intent)
    }
}