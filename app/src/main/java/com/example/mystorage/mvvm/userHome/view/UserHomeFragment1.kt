package com.example.mystorage.mvvm.userHome.view

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentUserHome1Binding

class UserHomeFragment1 : Fragment() {
    private lateinit var binding: FragmentUserHome1Binding
    private val handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserHome1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler.postDelayed({
            startAnimation(binding.textAnim1)
        }, 0)

        handler.postDelayed({
            startAnimation(binding.textAnim2)
        }, 1500)

        handler.postDelayed({
            startAnimation(binding.textAnim3)
        }, 3000)

        handler.postDelayed({
            startAnimation(binding.textAnim4)
        }, 4500)
    }

    private fun startAnimation(textView: TextView) {
        val anim = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
        textView.startAnimation(anim)
        textView.visibility = TextView.VISIBLE
    }
}