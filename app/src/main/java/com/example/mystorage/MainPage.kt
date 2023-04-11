package com.example.mystorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mystorage.databinding.ActivityMainPageBinding


class MainPage : AppCompatActivity() {
    private lateinit var binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(binding.navBar, findNavController(R.id.nav_host))

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
    }

}