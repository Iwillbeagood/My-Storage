package com.example.mystorage.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.mystorage.utils.Constants.TAG

object ActivityUtil {
    fun goToNextActivity(activity: Activity, nextActivity: Activity) {
        Log.d(TAG, "goToNextActivity() $activity to $nextActivity")
        val intent = Intent(activity, nextActivity::class.java)
        activity.startActivity(intent)
    }

    fun removeDialogFragment(activity: Activity) {

    }
}