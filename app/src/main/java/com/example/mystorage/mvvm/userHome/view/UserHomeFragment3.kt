package com.example.mystorage.mvvm.userHome.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystorage.MainPage
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentUserHome3Binding
import com.example.mystorage.mvvm.userHome.viewmodel.UserHomeViewModel
import com.example.mystorage.utils.ActivityUtil
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.CustomToast
import com.example.mystorage.adapter.HomeNameAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.json.JSONArray
import org.json.JSONObject

class UserHomeFragment3 : Fragment(), UserHomeIView, View.OnClickListener {
    private lateinit var binding: FragmentUserHome3Binding
    lateinit var sharedViewModel: UserHomeViewModel

    private var roomList = mutableListOf<String>()
    private var bathList = mutableListOf<String>()
    private val homeNameRoomAdapter = HomeNameAdapter(roomList, "room")
    private val homeNameBathAdapter = HomeNameAdapter(bathList, "rest")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "UserInformFragment3 - onCreateView() called")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_home3, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[UserHomeViewModel::class.java]
        binding.viewModel = sharedViewModel

        val roomLayoutManager = LinearLayoutManager(context)
        binding.roomNamesRecyclerView.layoutManager = roomLayoutManager
        binding.roomNamesRecyclerView.adapter = homeNameRoomAdapter

        val restLayoutManager = LinearLayoutManager(context)
        binding.bathNamesRecyclerView.layoutManager = restLayoutManager
        binding.bathNamesRecyclerView.adapter = homeNameBathAdapter

        binding.etcAddBtn.setOnClickListener(this)
        binding.saveUserInform.setOnClickListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.getRoomNum().observe(viewLifecycleOwner) { roomNum ->
            Log.d(TAG, "UserInformFragment3 - onViewCreated() - roomNum observed")
            roomList.clear()
            if (roomNum >= 1) {
                roomList.add("안방")
                for (i in 1 until roomNum)
                    roomList.add("방 $i")
                homeNameRoomAdapter.notifyDataSetChanged()
            }
        }
        sharedViewModel.getBathNum().observe(viewLifecycleOwner) { bathNum ->
            Log.d(TAG, "UserInformFragment3 - onViewCreated() - bathNum observed")
            bathList.clear()
            if (bathNum >= 1) {
                bathList.add("화장실")
                for (i in 1 until bathNum)
                    bathList.add("화장실 ${i + 1}")
            }
            homeNameBathAdapter.notifyDataSetChanged()
        }

        sharedViewModel.getUserHomeAddResult().observe(viewLifecycleOwner) { result ->
            when(result.first) {
                true -> onUserHomeInfoAddSuccess(result.second)
                false -> onUserHomeInfoAddError(result.second)
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.etcAddBtn -> {
                if (binding.etcAddBtn.isEnabled) {
                    binding.etcAddBtn.isEnabled = false
                    val string = binding.etcEdit.text
                    if (string.isNullOrEmpty()) {
                        return
                    } else {
                        binding.etcChipGroup.addView(Chip(requireContext()).apply {
                            text = string
                            isCloseIconVisible = true
                            setOnCloseIconClickListener { binding.etcChipGroup.removeView(this) }
                        })
                    }
                    binding.etcEdit.setText("")
                    binding.etcAddBtn.isEnabled = true
                }
            }
            R.id.saveUserInform -> {
                if (binding.saveUserInform.isEnabled) {
                    binding.saveUserInform.isEnabled = false
                    val jsonStringRoom = getJsonStringFromRecyclerView(binding.roomNamesRecyclerView, "room_names")
                    val jsonStringBath = getJsonStringFromRecyclerView(binding.bathNamesRecyclerView, "bath_names")
                    val etcName = getJsonStringFromChipGroup(binding.etcChipGroup)

                    binding.viewModel!!.setUserName(jsonStringRoom, jsonStringBath, etcName)
                    binding.viewModel!!.onUserInformAdd()
                    binding.saveUserInform.isEnabled = true
                }
            }
        }
    }

    private fun getJsonStringFromRecyclerView(recyclerView: RecyclerView, keyName: String): String {
        val jsonArray = JSONArray()
        for (i in 0 until recyclerView.childCount) {
            val viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i))
            val editText = viewHolder.itemView.findViewById<EditText>(R.id.strEditText)
            val text = editText.text.toString()
            jsonArray.put(text)
        }

        return JSONObject().put(keyName, jsonArray).toString()
    }

    private fun getJsonStringFromChipGroup(chipGroup: ChipGroup): String {
        val jsonArray = JSONArray()
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            jsonArray.put(chip.text.toString())
        }

        return JSONObject().put("etc_name", jsonArray).toString()
    }

    private fun onUserHomeInfoAddSuccess(message: String?) {
        CustomToast.createToast(requireActivity(), message.toString()).show()
        ActivityUtil.goToNextActivity(requireActivity(), MainPage())
    }

    private fun onUserHomeInfoAddError(message: String?) {
        CustomToast.createToast(requireActivity(), message.toString()).show()
    }
}