package com.example.mystorage.mvvm.userhome.view

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
import com.example.mystorage.databinding.FragmentUserInform3Binding
import com.example.mystorage.mvvm.userhome.viewmodel.UserInformViewModel
import com.example.mystorage.utils.ActivityUtil
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.adapter.HomeNameAdapter
import org.json.JSONArray
import org.json.JSONObject

class UserInformFragment3 : Fragment(), UserInformIView, View.OnClickListener {
    private lateinit var binding: FragmentUserInform3Binding
    lateinit var sharedViewModel: UserInformViewModel

    private var roomList = mutableListOf<String>()
    private var bathList = mutableListOf<String>()
    private val homeNameRoomAdapter = HomeNameAdapter(roomList, "room")
    private val homeNameBathAdapter = HomeNameAdapter(bathList, "rest")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "UserInformFragment3 - onCreateView() called")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_inform3, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[UserInformViewModel::class.java]
        binding.viewModel = sharedViewModel

        val roomLayoutManager = LinearLayoutManager(context)
        binding.roomNamesRecyclerView.layoutManager = roomLayoutManager
        binding.roomNamesRecyclerView.adapter = homeNameRoomAdapter

        val restLayoutManager = LinearLayoutManager(context)
        binding.bathNamesRecyclerView.layoutManager = restLayoutManager
        binding.bathNamesRecyclerView.adapter = homeNameBathAdapter

        binding.saveUserInform.setOnClickListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.getRoomNum().observe(viewLifecycleOwner) { roomNum ->
            Log.d(TAG, "UserInformFragment3 - onViewCreated() - roomNum observed")
            if (roomNum >= 1) {
                roomList.add("안방")
                for (i in 1 until roomNum)
                    roomList.add("방 $i")
                homeNameRoomAdapter.notifyDataSetChanged()
            }
        }
        sharedViewModel.getBathNum().observe(viewLifecycleOwner) { bathNum ->
            Log.d(TAG, "UserInformFragment3 - onViewCreated() - bathNum observed")
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
            R.id.saveUserInform -> {
                val jsonStringRoom = getJsonStringFromRecyclerView(binding.roomNamesRecyclerView, "room_names")
                val jsonStringBath = getJsonStringFromRecyclerView(binding.bathNamesRecyclerView, "bath_names")
                val etcName = binding.etcEdit.text.ifEmpty { "없음" }.toString()

                binding.viewModel!!.setUserName(jsonStringRoom, jsonStringBath, etcName)
                binding.viewModel!!.onUserInformAdd()
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

        val dataJsonObject = JSONObject()
        dataJsonObject.put(keyName, jsonArray)

        return dataJsonObject.toString()
    }

    private fun onUserHomeInfoAddSuccess(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        ActivityUtil.goToNextActivity(requireActivity(), MainPage())
    }

    private fun onUserHomeInfoAddError(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}