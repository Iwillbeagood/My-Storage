package com.example.mystorage.mvvm.userHome.view.changeStrName

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager.TAG
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentChangeStrNameBinding
import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.retrofit.model.UserHomeInfoResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.App
import com.example.mystorage.utils.CustomToast
import com.example.mystorage.utils.LoadInfoForSpinner
import com.example.mystorage.adapter.HomeNameChangeAdapter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeStrNameFragment : DialogFragment(), ChangeStrNameIView, View.OnClickListener {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(R.style.DialogLeftAnimation)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(R.color.white)
    }

    private lateinit var binding : FragmentChangeStrNameBinding
    private var homeList = mutableListOf<Pair<String, String>>()
    private val homeNameAdapter = HomeNameChangeAdapter(homeList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "ChangeUserHomeInfoFragment - onCreateView() called")
        binding = FragmentChangeStrNameBinding.inflate(inflater, container, false)
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
                        val roomItems  = loadedItems[0]
                        val bathItems = loadedItems[1]
                        val etcItems = loadedItems[2]
                        userHomeLoadResponse(roomItems, bathItems, etcItems)
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

    override fun userHomeLoadResponse(roomItems: MutableList<String>?, bathItems: MutableList<String>?, etcItems: MutableList<String>?) {
        if (roomItems!!.isNotEmpty()) {
            homeList.add(Pair("안방", roomItems.first()))

            for (i in 1 until roomItems.size)
                homeList.add(Pair("방 $i", roomItems[i]))
        }

        bathItems?.mapIndexed { i, name ->
            homeList.add(Pair("화장실 ${i + 1}", name))
        }

        etcItems?.mapIndexed { i, name ->
            homeList.add(Pair(name, name))
        }

        roomCnt = roomItems.size
        bathCnt = bathItems!!.size
        homeNameAdapter.notifyDataSetChanged()
    }

    // 바꾼 결과를 서버에 전송하기 위함
    override fun getJsonStringFromRecyclerView(recyclerView: RecyclerView) {
        val roomJsonArray = JSONArray()
        val roomDataJsonObject = JSONObject()
        for (i in 0 until roomCnt) {
            val viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i))
            val editText = viewHolder.itemView.findViewById<EditText>(R.id.str_change_editText)
            val text = editText.text.toString()
            roomJsonArray.put(text)
        }
        roomDataJsonObject.put("room_names", roomJsonArray)

        val bathJsonArray = JSONArray()
        val bathDataJsonObject = JSONObject()
        for (i in roomCnt until roomCnt + bathCnt) {
            val viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i))
            val editText = viewHolder.itemView.findViewById<EditText>(R.id.str_change_editText)
            val text = editText.text.toString()
            bathJsonArray.put(text)
        }
        bathDataJsonObject.put("bath_names", bathJsonArray)

        val etcJsonArray = JSONArray()
        val etcDataJsonObject = JSONObject()
        for (i in roomCnt + bathCnt until recyclerView.childCount) {
            val viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i))
            val editText = viewHolder.itemView.findViewById<EditText>(R.id.str_change_editText)
            val text = editText.text.toString()
            etcJsonArray.put(text)
        }
        etcDataJsonObject.put("etc_name", etcJsonArray)


        changeNameOfHome(roomDataJsonObject.toString(),bathDataJsonObject.toString(), etcDataJsonObject.toString())
    }

    override fun changeNameOfHome(jsonStringRoom: String, jsonStringBath: String, jsonStringEtc: String) {
        val api = RetrofitManager.getUserHomeNameChangeApiService()

        val call = api.changeHomeName(
            App.prefs.getString("userid", ""),
            jsonStringRoom,
            jsonStringBath,
            jsonStringEtc
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
        CustomToast.createToast(requireActivity(), message.toString()).show()
        dismiss()
    }

    override fun onUserHomeInfoChangeError(message: String?) {
        CustomToast.createToast(requireActivity(), message.toString()).show()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.changeUserHomeInformBack -> {
                if (binding.changeUserHomeInformBack.isEnabled) {
                    binding.changeUserHomeInformBack.isEnabled = false
                    Log.d(TAG, "ChangeUserHomeInfoFragment - onClick() called")
                    dismiss()
                    binding.changeUserHomeInformBack.isEnabled = true
                }
            }
            R.id.changeUserInform -> {
                if (binding.changeUserInform.isEnabled) {
                    binding.changeUserInform.isEnabled = false
                    Log.d(TAG, "ChangeUserHomeInfoFragment - changeUserInform onClick() called")
                    getJsonStringFromRecyclerView(binding.homeNamesRecyclerView)
                    binding.changeUserInform.isEnabled = true
                }
            }
        }
    }

}