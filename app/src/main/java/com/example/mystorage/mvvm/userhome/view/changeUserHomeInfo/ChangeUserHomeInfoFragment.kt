package com.example.mystorage.mvvm.userhome.view.changeUserHomeInfo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager.TAG
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentChangeUserHomeInfoBinding
import com.example.mystorage.retrofit.response.ApiResponse
import com.example.mystorage.retrofit.response.UserHomeInfoResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants
import com.example.mystorage.utils.LoadInfoForSpinner
import com.example.mystorage.utils.adapter.HomeNameAdapter
import com.example.mystorage.utils.adapter.HomeNameChangeAdapter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeUserHomeInfoFragment : DialogFragment(), ChangeUserHomeInfoIView, View.OnClickListener {

    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
    }

    private lateinit var binding : FragmentChangeUserHomeInfoBinding
    private var homeList = mutableListOf<Pair<String, String>>()
    private val homeNameAdapter = HomeNameChangeAdapter(homeList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "ChangeUserHomeInfoFragment - onCreateView() called")
        binding = FragmentChangeUserHomeInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.changeUserHomeInformBack.setOnClickListener(this)
        binding.changeUserInform.setOnClickListener(this)

        val layoutManager = LinearLayoutManager(context)
        binding.homeNamesRecyclerView.layoutManager = layoutManager
        binding.homeNamesRecyclerView.adapter = homeNameAdapter

        getResponseOnUserHomeLoad()
    }

    override fun getResponseOnUserHomeLoad() {
        val api = RetrofitManager.getUserHomeInfoLoadApiService()
        val call = api.loadUserHomeInfo(App.prefs.getString("userid", ""))
        call.enqueue(object : Callback<UserHomeInfoResponse> {
            override fun onResponse(call: Call<UserHomeInfoResponse>, response: Response<UserHomeInfoResponse>) {
                if (response.body() != null) {
                    try {
                        // 현재 사용자의 집 구조의 이름들를 가져옴
                        val loadedItems = LoadInfoForSpinner.userHomeInfoLoadResponseToChange(response.body()!!)
                        userHomeLoadResponse(loadedItems[0], loadedItems[1], loadedItems[2])
                    } catch (e: JSONException) {
                        Log.d(TAG, "응답 결과 파싱 중 오류가 발생했습니다")
                    }
                }
            }
            override fun onFailure(call: Call<UserHomeInfoResponse>, t: Throwable) {
                Log.d(TAG, "통신 실패")
                call.cancel()
            }
        })    }

    var roomCnt = 0
    var bathCnt = 0
    var etcCheck = false

    override fun userHomeLoadResponse(roomItems: MutableList<String>?, bathItems: MutableList<String>?, etcName: MutableList<String>?) {
        if (roomItems!!.isNotEmpty()) {
            homeList.add(Pair("안방", roomItems.first()))

            for (i in 1 until roomItems.size)
                homeList.add(Pair("방 $i", roomItems[i]))
        }

        bathItems?.mapIndexed { i, name ->
            homeList.add(Pair("화장실 ${i + 1}", name))
        }

        if (etcName?.first() != "") {
            etcCheck = true
            homeList.add(Pair("기타", etcName.toString()))
        }


        roomCnt = roomItems.size
        bathCnt = bathItems!!.size
        homeNameAdapter.notifyDataSetChanged()
    }

    private fun getJsonStringFromRecyclerView(recyclerView: RecyclerView) {
        val roomJsonArray = JSONArray()
        val bathJsonArray = JSONArray()
        val roomDataJsonObject = JSONObject()
        val bathDataJsonObject = JSONObject()
        for (i in 0 until roomCnt) {
            val viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i))
            val editText = viewHolder.itemView.findViewById<EditText>(R.id.str_change_editText)
            val text = editText.text.toString()
            roomJsonArray.put(text)
        }

        roomDataJsonObject.put("room_names", roomJsonArray)

        for (i in roomCnt until roomCnt + bathCnt) {
            val viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i))
            val editText = viewHolder.itemView.findViewById<EditText>(R.id.str_change_editText)
            val text = editText.text.toString()
            bathJsonArray.put(text)
        }

        bathDataJsonObject.put("bath_names", bathJsonArray)

        var changeEtcName = ""
        if (etcCheck) {
            val viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(recyclerView.childCount))
            val editText = viewHolder.itemView.findViewById<EditText>(R.id.str_change_editText)
            changeEtcName = editText.text.toString()
        }

        changeNameOfHome(roomDataJsonObject.toString(),bathDataJsonObject.toString(), changeEtcName)
    }

    private fun changeNameOfHome(jsonStringRoom: String, jsonStringBath: String, etcName: String?) {
        val api = RetrofitManager.getUserHomeInfoChangeApiService()

        val call = api.changeUserHomeInfo(
            App.prefs.getString("userid", ""),
            jsonStringRoom,
            jsonStringBath,
            etcName.toString()
            )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        userHomeChangeResponse(response.body()!!)
                    } catch (e: JSONException) {
                        onUserHomeInfoChangeError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                onUserHomeInfoChangeError("통신 실패")
                call.cancel()
            }
        })
    }

    override fun userHomeChangeResponse(response: ApiResponse) {
        val message = response.message
        if (response.status == "true") {
            onUserHomeInfoChangeSuccess(message)
        } else {
            onUserHomeInfoChangeError(message)
        }
    }

    override fun onUserHomeInfoChangeSuccess(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        dismiss()
    }

    override fun onUserHomeInfoChangeError(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.changeUserHomeInformBack -> {
                Log.d(TAG, "ChangeUserHomeInfoFragment - onClick() called")
                dismiss()
            }
            R.id.changeUserInform -> {
                Log.d(TAG, "ChangeUserHomeInfoFragment - changeUserInform onClick() called")
                getJsonStringFromRecyclerView(binding.homeNamesRecyclerView)
            }
        }
    }

}