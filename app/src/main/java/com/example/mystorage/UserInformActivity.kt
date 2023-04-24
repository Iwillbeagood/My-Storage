package com.example.mystorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.mystorage.databinding.ActivityUserInformBinding
import com.example.mystorage.mvvm.userhome.view.UserInformFragment1
import com.example.mystorage.mvvm.userhome.view.UserInformFragment2
import com.example.mystorage.mvvm.userhome.view.UserInformFragment3
import com.example.mystorage.mvvm.userhome.view.UserInformIView
import com.example.mystorage.mvvm.userhome.viewmodel.UserInformViewModel
import com.example.mystorage.mvvm.userhome.viewmodel.UserInformViewModelFactory

class UserInformActivity : AppCompatActivity(), UserInformIView {
    private lateinit var binding: ActivityUserInformBinding
    private val userInformFragment2 = UserInformFragment2()
    private val userInformFragment3 = UserInformFragment3()
    private lateinit var sharedViewModel: UserInformViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(supportFragmentManager)

        binding.viewPager.adapter = adapter

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                userInformFragment2.getSelectedChipValue()
            }
        })
        val userInformViewModelFactory = UserInformViewModelFactory(this)
        sharedViewModel = ViewModelProvider(this, userInformViewModelFactory)[UserInformViewModel::class.java]

        // 두 개의 프래그먼트에서 공유할 뷰모델 인스턴스 설정
        userInformFragment2.sharedViewModel = sharedViewModel
        userInformFragment3.sharedViewModel = sharedViewModel
    }

    inner class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragmentList = listOf(
            UserInformFragment1(),
            userInformFragment2,
            UserInformFragment3()
        )

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }
    }
}
