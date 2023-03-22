package com.example.mystorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.example.mystorage.databinding.ActivityMainPageBinding
import com.example.mystorage.fragment.ItemListFragment
import com.example.mystorage.fragment.ItemStructureFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainPage : AppCompatActivity() {
    private var mBinding: ActivityMainPageBinding? = null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationBar.inflateMenu(R.menu.bottom_navigation)
        binding.bottomNavigationBar.background = null
        binding.bottomNavigationBar.menu.getItem(2).isEnabled = false

        fragmentReplace(ItemListFragment())

        binding.bottomNavigationBar.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.action_list -> fragmentReplace(ItemListFragment())
                    R.id.action_structure -> fragmentReplace(ItemStructureFragment())
                }
                true
            }
        }
    }

    private fun fragmentReplace(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameContainer, fragment)
            .commit()
    }


    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}