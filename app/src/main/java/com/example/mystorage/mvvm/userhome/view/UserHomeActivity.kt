package com.example.mystorage.mvvm.userhome.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.mystorage.databinding.ActivityUserHomeBinding
import com.example.mystorage.mvvm.userhome.viewmodel.UserHomeViewModel
import com.example.mystorage.mvvm.userhome.viewmodel.UserHomeViewModelFactory

class UserHomeActivity : AppCompatActivity(), UserHomeIView {
    private lateinit var binding: ActivityUserHomeBinding
    private val userHomeFragment1 = UserHomeFragment1()
    private val userHomeFragment2 = UserHomeFragment2()
    private val userHomeFragment3 = UserHomeFragment3()
    private lateinit var sharedViewModel: UserHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(supportFragmentManager)

        binding.viewPager.adapter = adapter

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                userHomeFragment2.getSelectedChipValue()
            }
        })
        val userHomeViewModelFactory = UserHomeViewModelFactory(this)
        sharedViewModel = ViewModelProvider(this, userHomeViewModelFactory)[UserHomeViewModel::class.java]

        // 두 개의 프래그먼트에서 공유할 뷰모델 인스턴스 설정
        userHomeFragment2.sharedViewModel = sharedViewModel
        userHomeFragment3.sharedViewModel = sharedViewModel
    }

    inner class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragmentList = listOf(
            userHomeFragment1,
            userHomeFragment2,
            userHomeFragment3
        )

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }
    }
}
