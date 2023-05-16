package com.example.mystorage.mvvm.userHome.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.mvvm.userHome.model.UserHome
import com.example.mystorage.mvvm.userHome.view.UserHomeIView
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants.TAG
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserHomeViewModel(private val serInformIView: UserHomeIView): ViewModel(), UserHomeIViewModel {
    private val userHome = UserHome("", "", "", "", "", "")

    private val _roomNum = MutableLiveData<Int>()
    private val roomNum: LiveData<Int> = _roomNum

    private val _bathNum = MutableLiveData<Int>()
    private val bathNum: LiveData<Int> = _bathNum

    private val _userHomeAddResult = MutableLiveData<Pair<Boolean, String>>()
    private val userHomeAddResult: LiveData<Pair<Boolean, String>> = _userHomeAddResult

    override fun setUserHomeNum(roomNum: Int, bathNum: Int) {
        // MutableLiveData 객체에 데이터를 할당합니다.
        _roomNum.value = roomNum
        _bathNum.value = bathNum
    }

    override fun getRoomNum(): LiveData<Int> {
        return roomNum
    }

    override fun getBathNum(): LiveData<Int> {
        return bathNum
    }

    override fun getUserHomeAddResult(): LiveData<Pair<Boolean, String>> {
        return userHomeAddResult
    }

    override fun setUserHome(livingroomName: String, kitchen: String, storage: String) {
        userHome.setLivingroom(livingroomName)
        userHome.setKitchen(kitchen)
        userHome.setStorage(storage)
    }

    override fun setUserName(roomNames: String, bathroomNames: String, etcName: String) {
        userHome.setRoomNames(roomNames)
        userHome.setBathroomNames(bathroomNames)
        userHome.setEtcName(etcName)
    }

    override fun onUserInformAdd() {
        getResponseOnUserHomeAdd()
    }

    override fun getResponseOnUserHomeAdd() {
        val api = RetrofitManager.getUserInfoHomeAddApiService()

        val call = api.addUserHomeInfo(
            App.prefs.getString("userid", ""),
            userHome.getLivingroom().toString(),
            userHome.getKitchen().toString(),
            userHome.getStorage().toString(),
            userHome.getRoomNames().toString(),
            userHome.getBathroomNames().toString(),
            userHome.getEtcName().toString()
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        userHomeResponse(response.body()!!)
                    } catch (e: JSONException) {
                        _userHomeAddResult.value = Pair(false, "응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _userHomeAddResult.value = Pair(false, "통신 실패")
                call.cancel()
            }
        })
    }

    override fun userHomeResponse(response: ApiResponse) {
        val message = response.message
        Log.d(TAG, "UserInformViewModel - userHomeResponse() called - response: $message")
        when (response.status) {
            "true" -> _userHomeAddResult.value = Pair(true, message)
            "false" -> _userHomeAddResult.value = Pair(false, message)
        }
    }

}