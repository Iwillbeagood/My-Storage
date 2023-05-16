package com.example.mystorage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.*
import com.example.mystorage.adapter.MyPagerAdapter
import com.example.mystorage.databinding.ActivityMainPageBinding
import com.example.mystorage.mvvm.item.view.HowToAddItemFragment
import com.example.mystorage.mvvm.item.view.autoItemAdd.AutoItemAddFragment
import com.example.mystorage.mvvm.item.view.itemAdd.ItemAddDialogFragment
import com.example.mystorage.mvvm.item.view.itemList.ItemListFragment
import com.example.mystorage.mvvm.item.view.itemStructure.ItemStructureFragment
import com.example.mystorage.mvvm.item.view.itemUpdate.ItemUpdateFragment
import com.example.mystorage.mvvm.user.view.signOut.SignOutDialogFragment
import com.example.mystorage.mvvm.userHome.view.UserHomeActivity
import com.example.mystorage.mvvm.userHome.view.changeStrName.ChangeStrNameFragment
import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.*
import com.example.mystorage.utils.Constants.TAG
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

    private lateinit var homeNameChangeView : TextView
    private lateinit var strReset : TextView
    private lateinit var deleteAllItem : TextView
    private lateinit var logoutView : TextView
    private lateinit var signOutView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addItemFab.setOnClickListener(this)

        // 메뉴 아이템 설정
        homeNameChangeView = findViewById<TextView>(R.id.menu_item_change_home_name)
        strReset = findViewById<TextView>(R.id.menu_item_str_reset)
        deleteAllItem = findViewById<TextView>(R.id.menu_item_delete_all)
        logoutView = findViewById<TextView>(R.id.menu_logout)
        signOutView = findViewById<TextView>(R.id.menu_sign_out)
        homeNameChangeView.setOnClickListener(this)
        strReset.setOnClickListener(this)
        deleteAllItem.setOnClickListener(this)
        logoutView.setOnClickListener(this)
        signOutView.setOnClickListener(this)

        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                // Handle the back button event
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

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
        binding.tabLayout.getTabAt(2)?.setIcon(R.drawable.box_unpacked_svgrepo_com)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.addItemFab -> {
                if (binding.addItemFab.isEnabled) {
                    binding.addItemFab.isEnabled = false
                    Log.d(TAG, "MainPage - addItemFab onClick() called")
                    val howToAddItemFragment = HowToAddItemFragment()
                    howToAddItemFragment.show(supportFragmentManager, "HowToAddItemFragment")
                    binding.addItemFab.isEnabled = true
                }
            }
            R.id.menu_item_change_home_name -> {
                if (homeNameChangeView.isEnabled) {
                    homeNameChangeView.isEnabled = false
                    Log.d(TAG, "MainPage - menu_item_change_home_info onClick() called")
                    val changeUserHomeInfoFragment = ChangeStrNameFragment()
                    changeUserHomeInfoFragment.show(supportFragmentManager, "ChangeUserHomeInfoFragment")
                    homeNameChangeView.isEnabled = true
                }
            }
            R.id.menu_item_str_reset -> {
                if (strReset.isEnabled) {
                    strReset.isEnabled = false
                    Log.d(TAG, "MainPage - menu_item_str_reset onClick() called")
                    DialogUtils.showNoMessageDialog(
                        this,
                        "구조 설정을 초기화하고 재설정합니다",
                        "확인",
                        "취소",
                        onPositiveClick = {
                            // 기존의 설정 삭제
                            getUserInfoDelete()
                        },
                        onNegativeClick = {
                        }
                    )
                    strReset.isEnabled = true
                }
            }
            R.id.menu_item_delete_all -> {
                if (deleteAllItem.isEnabled) {
                    deleteAllItem.isEnabled = false
                    Log.d(TAG, "MainPage - menu_item_delete_all onClick() called")
                    DialogUtils.showNoMessageDialog(
                        this,
                        "물건을 창고에서 모두 제거합니다",
                        "확인",
                        "취소",
                        onPositiveClick = {
                            // 창고에서 물건 제거
                            getDeleteAllItem()
                        },
                        onNegativeClick = {
                        }
                    )
                    deleteAllItem.isEnabled = true
                }
            }
            R.id.menu_logout -> {
                if (logoutView.isEnabled) {
                    logoutView.isEnabled = false
                    Log.d(TAG, "MainPage - menu_logout onClick() called")
                    DialogUtils.showNoMessageDialog(
                        this,
                        "로그아웃",
                        "확인",
                        "취소",
                        onPositiveClick = {
                            App.prefs.removeString("userid")
                            App.prefs.removeString("userpassword")
                            CustomToast.createToast(this, "로그아웃").show()
                            ActivityUtil.goToNextActivity(this, MainActivity())
                        },
                        onNegativeClick = {
                        }
                    )
                    logoutView.isEnabled = true
                }
            }
            R.id.menu_sign_out -> {
                if (signOutView.isEnabled) {
                    signOutView.isEnabled = false
                    Log.d(TAG, "MainPage - menu_sign_out onClick() called")
                    val signOutDialogFragment = SignOutDialogFragment()
                    signOutDialogFragment.show(supportFragmentManager, "SignOutDialogFragment")
                    signOutView.isEnabled = true
                }
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
                        Log.d(TAG, "MainPage - getUserInfo() - onResponse() 응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "MainPage - getUserInfo() - onFailure() 통신 실패")
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
                Log.d(TAG, "MainPage - userInfoResponse() $message called")
            }
        }
    }

    private fun getUserInfoDelete() {
        val api = RetrofitManager.getUserHomeInfoDeleteApiService()

        val call = api.userHomeInfoDelete(
            App.prefs.getString("userid", ""),
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        userInfoDeleteResponse(response.body()!!)
                    } catch (e: JSONException) {
                        Log.d(TAG, "MainPage - getUserInfoDelete() - onResponse() 응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "MainPage - getUserInfoDelete() - onFailure() 통신 실패")
                call.cancel()
            }
        })
    }

    private fun userInfoDeleteResponse(response: ApiResponse) {
        val message = response.message
        CustomToast.createToast(this, message).show()
        when (response.status) {
            "true" -> {
                // 초반 설정을 다시하기 위해 설정 페이지로 이동
                ActivityUtil.goToNextActivity(this, UserHomeActivity())
            }
            "false" -> {
                // 삭제에 실패했음으로 로그 출력
                Log.d(TAG, "MainPage - userInfoDeleteResponse() $message called")
            }
        }
    }

    private fun getDeleteAllItem() {
        val api = RetrofitManager.getItemDeleteAllApiService()

        val call = api.deleteAllItem(
            App.prefs.getString("userid", ""),
            "items"
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        deleteAllItemResponse(response.body()!!)
                    } catch (e: JSONException) {
                        Log.d(TAG, "MainPage - getDeleteAllItem() - onResponse() 응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "MainPage - getDeleteAllItem() - onFailure() 통신 실패")
                call.cancel()
            }
        })
    }

    private fun deleteAllItemResponse(response: ApiResponse) {
        val message = response.message
        CustomToast.createToast(this, message).show()
        when (response.status) {
            "true" -> {
                // 삭제된 아이템을 반영하기 위함
                val itemStrFragment = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":0") as ItemStructureFragment?
                itemStrFragment?.onResume()
                val itemListFragment = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":1") as ItemListFragment?
                itemListFragment?.onResume()

                ActivityUtil.goToNextActivity(this, MainPage())
            }
            "false" -> {
                // 삭제에 실패했음으로 로그 출력
                Log.d(TAG, "MainPage - deleteAllItemResponse() $message called")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}