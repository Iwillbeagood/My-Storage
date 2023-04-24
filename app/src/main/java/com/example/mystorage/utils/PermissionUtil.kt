package com.example.mystorage.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mystorage.utils.Constants.TAG

object PermissionUtil {
    const val CAMERA_PERMISSION_REQUEST_CODE = 100
    const val CAMERA_REQUEST_CODE = 102
    const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 200
    const val STORAGE_REQUEST_CODE = 202


    fun requestCameraPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            openCamera(activity)
        }
    }

    fun requestReadExternalStoragePermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        } else {
            openGallery(activity)
        }
    }

    fun shouldShowCameraPermissionRationale(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)
    }

    fun shouldShowReadExternalStoragePermissionRationale(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun handlePermissionsResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray, onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera(activity)
                } else {
                    onPermissionDenied.invoke()
                }
            }
            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery(activity)
                } else {
                    onPermissionDenied.invoke()
                }
            }
        }
    }

    fun openCamera(activity: Activity) {
        Log.d(TAG, "PermissionUtil - openCamera() called")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity.startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    fun openGallery(activity: Activity) {
        Log.d(TAG, "PermissionUtil - openGallery() called")
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        activity.startActivityForResult(intent, STORAGE_REQUEST_CODE)
    }

}
