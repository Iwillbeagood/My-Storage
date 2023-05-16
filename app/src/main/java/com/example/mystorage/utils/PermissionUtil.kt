package com.example.mystorage.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mystorage.utils.Constants.TAG

object PermissionUtil {
    const val CAMERA_PERMISSION_REQUEST_CODE = 100
    const val CAMERA_REQUEST_CODE = 102
    const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 200
    const val STORAGE_REQUEST_CODE = 202

    fun requestReadExternalStoragePermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder(activity)
                    .setTitle("저장소 읽기 권한이 필요합니다")
                    .setMessage("이 기능을 사용하려면 저장소 읽기 권한이 필요합니다.")
                    .setPositiveButton("확인") { _, _ ->
                        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
                    }
                    .setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            } else {
            // 이전에 권한 요청을 거부한 경우
                AlertDialog.Builder(activity)
                    .setTitle("저장소 읽기 권한이 필요합니다")
                    .setMessage("이 기능을 사용하려면 저장소 읽기 권한이 필요합니다. 설정에서 권한을 허용해주세요.")
                    .setPositiveButton("확인") { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", activity.packageName, null)
                        intent.data = uri
                        activity.startActivityForResult(intent, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
                    }
                    .setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        } else {
            openGallery(activity)
        }
    }

    fun shouldShowReadExternalStoragePermissionRationale(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun shouldShowCameraPermissionRationale(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)
    }

    fun requestCameraPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                AlertDialog.Builder(activity)
                    .setTitle("카메라 권한이 필요합니다")
                    .setMessage("이 기능을 사용하려면 카메라 권한이 필요합니다.")
                    .setPositiveButton("확인") { _, _ ->
                        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
                    }
                    .setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            } else {
                // 다이얼로그를 띄우기 위해 권한 요청을 반복합니다.
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            }
        } else {
            openCamera(activity)
        }
    }


    fun handlePermissionsResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (shouldShowCameraPermissionRationale(activity)) {
                    CustomToast.createToast(activity, "카메라 권한이 필요합니다").show()
                } else {
                    requestCameraPermission(activity)
                }
            }
            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery(activity)
                } else {
                    if (shouldShowReadExternalStoragePermissionRationale(activity)) {
                        CustomToast.createToast(activity, "외부 저장소 읽기 권한이 필요합니다").show()

                    } else {
                        requestReadExternalStoragePermission(activity)
                    }
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
        intent.type = "image/*"
        activity.startActivityForResult(intent, STORAGE_REQUEST_CODE)
    }
}
