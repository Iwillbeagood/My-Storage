package com.example.mystorage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.*
import com.example.mystorage.databinding.ActivityMainPageBinding
import com.example.mystorage.mvvm.item.view.HowToAddItemFragment
import com.example.mystorage.mvvm.item.view.autoItemAdd.AutoItemAddFragment
import com.example.mystorage.mvvm.item.view.itemAdd.ItemAddDialogFragment
import com.example.mystorage.mvvm.item.view.itemList.ItemListFragment
import com.example.mystorage.mvvm.item.view.itemStructure.ItemStructureFragment
import com.example.mystorage.mvvm.item.view.itemUpdate.ItemUpdateFragment
import com.example.mystorage.mvvm.user.view.signOut.SignOutDialogFragment
import com.example.mystorage.mvvm.userhome.view.changeUserHomeInfo.ChangeUserHomeInfoFragment
import com.example.mystorage.retrofit.response.ApiResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.ActivityUtil
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.DialogUtils
import com.example.mystorage.utils.PermissionUtil
import org.json.JSONException
import org.opencv.android.OpenCVLoader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainPage : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainPageBinding

    init {
        val isInitialized = OpenCVLoader.initDebug()
        Log.d(TAG, "isInitialized = $isInitialized")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addItemFab.setOnClickListener(this)

        // 메뉴 아이템 설정
        val homeInfoChangeView = findViewById<TextView>(R.id.menu_item_change_home_name)
        val logoutView = findViewById<TextView>(R.id.menu_logout)
        val signOutView = findViewById<TextView>(R.id.menu_sign_out)
        homeInfoChangeView.setOnClickListener(this)
        logoutView.setOnClickListener(this)
        signOutView.setOnClickListener(this)

        setupViewPager()
        setupTabLayout()
        getUserInfo()
    }

    private fun setupViewPager() {
        val pagerAdapter = MyPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
    }

    private fun setupTabLayout() {
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_baseline_warehouse_24)
        binding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_baseline_format_list_bulleted_24)
    }

    private inner class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> ItemStructureFragment()
                1 -> ItemListFragment()
                else -> throw IllegalArgumentException("Invalid position $position")
            }
        }
        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position) {
                0 -> "구조"
                1 -> "목록"
                else -> null
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.addItemFab -> {
                Log.d(TAG, "MainPage - addItemFab onClick() called")
                val howToAddItemFragment = HowToAddItemFragment()
                howToAddItemFragment.show(supportFragmentManager, "HowToAddItemFragment")
            }
            R.id.menu_item_change_home_name -> {
                Log.d(TAG, "MainPage - menu_item_change_home_info onClick() called")
                val changeUserHomeInfoFragment = ChangeUserHomeInfoFragment()
                changeUserHomeInfoFragment.show(supportFragmentManager, "ChangeUserHomeInfoFragment")
            }
            R.id.menu_logout -> {
                Log.d(TAG, "MainPage - menu_logout onClick() called")
                DialogUtils.showNoMessageDialog(
                    this,
                    "로그아웃",
                    "확인",
                    "취소",
                    onPositiveClick = {
                        App.prefs.removeString("userid")
                        App.prefs.removeString("userpassword")
                        ActivityUtil.goToNextActivity(this, MainActivity())
                    },
                    onNegativeClick = {
                    }
                )
            }
            R.id.menu_sign_out -> {
                Log.d(TAG, "MainPage - menu_sign_out onClick() called")
                val signOutDialogFragment = SignOutDialogFragment()
                signOutDialogFragment.show(supportFragmentManager, "SignOutDialogFragment")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "MainPage - onActivityResult() called 사용자가 선택한 데이터 프래그먼트로 전송")

        removeDialogFragment("CameraGalleryDialogFragment")

        val itemAddDialogFragment = supportFragmentManager.findFragmentByTag("ItemAddDialogFragment") as? ItemAddDialogFragment
        val itemUpdateFragment = supportFragmentManager.findFragmentByTag("ItemUpdateFragment") as? ItemUpdateFragment
        val autoItemAddFragment = supportFragmentManager.findFragmentByTag("AutoItemAddFragment") as? AutoItemAddFragment

        when (requestCode) {
            PermissionUtil.CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val imageBitmap = data.extras?.get("data") as Bitmap
                    itemAddDialogFragment?.addImageBitmap(imageBitmap)
                    autoItemAddFragment?.addReceiptBitmap(imageBitmap)
                    itemUpdateFragment?.updateImageBitmap(imageBitmap)
                }
            }
            PermissionUtil.STORAGE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val imageUri = data.data
                    itemAddDialogFragment?.addImageURL(imageUri!!)
                    autoItemAddFragment?.addReceiptURL(imageUri!!)
                    itemUpdateFragment?.updateImageURL(imageUri!!)
                }
            }
        }
    }

    private fun removeDialogFragment(tag: String) {
        Log.d(TAG, "MainPage - removeDialogFragment() called $tag is dismissed")
        val temp = supportFragmentManager.findFragmentByTag(tag)
        if (temp != null) {
            val dialogFragment = temp as DialogFragment
            dialogFragment.dismiss()
        }
    }

    fun restartItemListFragment() {
        Log.d(TAG, "MainPage - restartItemListFragment() called")
        val pagerAdapter = binding.viewPager.adapter as? MyPagerAdapter
        pagerAdapter?.let {
            val fragment = it.getItem(1) // 1 is the position of ItemListFragment
            supportFragmentManager.beginTransaction().apply {
                detach(fragment)
                attach(fragment)
                commit()
            }
        }
    }

    fun showDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }


    private fun getUserInfo() {
        val api = RetrofitManager.getUserInfoApiService()

        val call = api.loadUserInfo(
            App.prefs.getString("userid", ""),
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        userInfoResponse(response.body()!!)
                    } catch (e: JSONException) {
                        Log.d(TAG, "MainPage - onResponse() 응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "MainPage - onFailure() 통신 실패")
                call.cancel()
            }
        })
    }

    private fun userInfoResponse(response: ApiResponse) {
        val message = response.message
        when (response.status) {
            "true" -> {
                val userNameView = findViewById<TextView>(R.id.userNameView)
                userNameView.text = "${message}의 창고"
            }
            "false" -> {
                // 회원 탈퇴를 하지 못했음으로 메시지 출력
                Log.d(TAG, "MainPage - $message")
            }
        }
    }
}