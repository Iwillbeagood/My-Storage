package com.example.mystorage

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mystorage.databinding.ActivityMainPageBinding
import com.example.mystorage.mvvm.item.view.itemAddView.ItemAddDialogFragment
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.PermissionUtil
import java.io.ByteArrayOutputStream
import java.io.InputStream


class MainPage : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(binding.navBar, findNavController(R.id.nav_host))

        binding.navBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_list -> {
                    findNavController(R.id.nav_host).navigate(R.id.itemListFragment)
                    true
                }
                R.id.action_structure -> {
                    findNavController(R.id.nav_host).navigate(R.id.itemStructureFragment)
                    true
                }
                else -> false
            }
        }
        binding.addItemFab.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.addItemFab -> {
                ItemAddDialogFragment().show(supportFragmentManager, "ItemAddDialogFragment")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "MainPage - onActivityResult() called 사용자가 선택한 데이터 프래그먼트로 전송")

        removeDialogFragment("CameraGalleryDialogFragment")

        val fragment = supportFragmentManager.findFragmentByTag("ItemAddDialogFragment") as? ItemAddDialogFragment

        when (requestCode) {
            PermissionUtil.CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val imageBitmap = data.extras?.get("data") as Bitmap
                    fragment?.updateImageBitmap(imageBitmap)
                }
            }
            PermissionUtil.STORAGE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val imageUri = data.data
                    fragment?.updateImageURL(imageUri!!)

                }
            }
        }
    }

    fun removeDialogFragment(tag: String) {
        Log.d(TAG, "MainPage - removeDialogFragment() called $tag is dismissed")
        val temp = supportFragmentManager.findFragmentByTag(tag)
        if (temp != null) {
            val dialogFragment = temp as DialogFragment
            dialogFragment.dismiss()
        }
    }

}