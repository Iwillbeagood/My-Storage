package com.example.mystorage.mvvm.userhome.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.MainActivity
import com.example.mystorage.R
import com.example.mystorage.UserInformActivity
import com.example.mystorage.databinding.FragmentUserInform2Binding
import com.example.mystorage.mvvm.userhome.viewmodel.UserInformViewModel
import com.example.mystorage.mvvm.userhome.viewmodel.UserInformViewModelFactory
import com.example.mystorage.utils.Constants.TAG
import com.google.android.material.chip.Chip

class UserInformFragment2 : Fragment(), UserInformIView {
    private lateinit var binding : FragmentUserInform2Binding
    lateinit var sharedViewModel: UserInformViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "UserInformFragment2 - onCreateView() called")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_inform2, container, false)

        sharedViewModel = ViewModelProvider(requireActivity())[UserInformViewModel::class.java]
        binding.viewModel = sharedViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "UserInformFragment2 - onViewCreated() called")

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

        // 만약 "없음"이 선택된 경우, "0"으로 바꾸기
        selectedValueList[0] = if (selectedValueList[0] == "없음") "0" else selectedValueList[0]
        selectedValueList[1] = if (selectedValueList[1] == "없음") "0" else selectedValueList[1]

        Log.d(TAG, "UserInformFragment2 - num - ${selectedValueList[0]} ${selectedValueList[1]}")

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
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}