package com.example.mystorage.mvvm.userHome.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentUserHome2Binding
import com.example.mystorage.mvvm.userHome.viewmodel.UserHomeViewModel
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.CustomToast
import com.google.android.material.chip.Chip

class UserHomeFragment2 : Fragment(), UserHomeIView {
    private lateinit var binding : FragmentUserHome2Binding
    lateinit var sharedViewModel: UserHomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "UserInformFragment2 - onCreateView() called")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_home2, container, false)

        sharedViewModel = ViewModelProvider(requireActivity())[UserHomeViewModel::class.java]
        binding.viewModel = sharedViewModel

        return binding.root
    }

    fun getSelectedChipValue() {
        val chipGroupList = listOf(
            binding.chipGroupRoom,
            binding.chipGroupBath,
            binding.chipGroupLiving,
            binding.chipGroupKitchen,
            binding.chipGroupStorage
        )

        val selectedValueList = mutableListOf<String>()

        for (chipGroup in chipGroupList) {
            val selectedId = chipGroup.checkedChipId
            val selectedChip = chipGroup.findViewById<Chip>(selectedId)
            if (selectedId == View.NO_ID) {
                onChipSelectedError(findChipGroupType(chipGroup.id))
                return
            }
            val selectedText = selectedChip?.text?.toString() ?: "0"

            // 선택된 텍스트를 리스트에 추가
            selectedValueList.add(selectedText)
        }

        // 방, 화장실의 개수를 모델에 저장
        binding.viewModel!!.setUserHomeNum(selectedValueList[0].toInt(), selectedValueList[1].toInt())
        // 거실, 주방, 창고 유무를 모델에 저장
        binding.viewModel!!.setUserHome(selectedValueList[2], selectedValueList[3], selectedValueList[4])
    }

    private fun findChipGroupType(chipGroup: Int): String {
        return when(chipGroup) {
            R.id.chipGroupRoom -> "방 개수를 입력해 주세요"
            R.id.chipGroupBath -> "화장실 개수를 입력해 주세요"
            R.id.chipGroupLiving -> "거실 유/무를 입력해 주세요"
            R.id.chipGroupKitchen -> "주방 유/무를 입력해 주세요"
            R.id.chipGroupStorage -> "창고 유/무를 입력해 주세요"
            else -> ""
        }
    }

    private fun onChipSelectedError(message: String?) {
        CustomToast.createToast(requireActivity(), message.toString()).show()
    }
}