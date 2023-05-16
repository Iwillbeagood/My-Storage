package com.example.mystorage.mvvm.item.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentCameraGalleryDialogBinding
import com.example.mystorage.utils.BitmapConverter
import com.example.mystorage.utils.Constants
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.CustomToast
import com.example.mystorage.utils.PermissionUtil
import com.example.mystorage.utils.PermissionUtil.CAMERA_PERMISSION_REQUEST_CODE
import com.example.mystorage.utils.PermissionUtil.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE

class CameraGalleryDialogFragment : DialogFragment(), View.OnClickListener {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 500)
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
    }

    private lateinit var binding: FragmentCameraGalleryDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "CameraGalleryDialogFragment - onCreateView() called")
        binding = FragmentCameraGalleryDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cameraLayout.setOnClickListener(this)
        binding.galleryLayout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.cameraLayout -> {
                if (binding.cameraLayout.isEnabled) {
                    binding.cameraLayout.isEnabled = false
                    PermissionUtil.handlePermissionsResult(requireActivity(), 100,
                        arrayOf(Manifest.permission.CAMERA),
                        intArrayOf(PackageManager.PERMISSION_GRANTED))
                    binding.cameraLayout.isEnabled = true
                }
            }
            R.id.galleryLayout -> {
                if (binding.galleryLayout.isEnabled) {
                    binding.galleryLayout.isEnabled = false
                    PermissionUtil.handlePermissionsResult(requireActivity(), 200,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        intArrayOf(PackageManager.PERMISSION_GRANTED))
                    binding.galleryLayout.isEnabled = true
                }
            }
        }
    }
}
