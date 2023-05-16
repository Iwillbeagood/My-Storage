package com.example.mystorage.mvvm.item.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager.TAG
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentHowToAddItemBinding
import com.example.mystorage.mvvm.item.view.autoItemAdd.AutoItemAddFragment
import com.example.mystorage.mvvm.item.view.itemAdd.ItemAddDialogFragment

class HowToAddItemFragment : DialogFragment(), View.OnClickListener {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(900, 500)
    }

    private lateinit var binding: FragmentHowToAddItemBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "HowToAddItemFragment - onCreateView() called")
        binding = FragmentHowToAddItemBinding.inflate(inflater, container, false)
        binding.ocrLayout.setOnClickListener(this)
        binding.selfInputLayout.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.ocrLayout -> {
                if (binding.ocrLayout.isEnabled) {
                    binding.ocrLayout.isEnabled = false
                    Log.d(TAG, "HowToAddItemFragment - ocrLayout onClick() called")
                    dismiss()
                    val autoItemAddFragment = AutoItemAddFragment()
                    autoItemAddFragment.show(parentFragmentManager, "AutoItemAddFragment")
                    binding.ocrLayout.isEnabled = true
                }
            }
            R.id.selfInputLayout -> {
                if (binding.selfInputLayout.isEnabled) {
                    binding.selfInputLayout.isEnabled = false
                    Log.d(TAG, "HowToAddItemFragment - selfInputLayout onClick() called")
                    dismiss()
                    val itemAddDialogFragment = ItemAddDialogFragment()
                    itemAddDialogFragment.show(parentFragmentManager, "ItemAddDialogFragment")
                    binding.selfInputLayout.isEnabled = true
                }

             }
        }
    }

}