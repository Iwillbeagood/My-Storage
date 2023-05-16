package com.example.mystorage.retrofit.retrofitManager

import android.content.Context
import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.retrofit.model.UserHomeInfoResponse
import com.example.mystorage.utils.App
import com.example.mystorage.utils.LoadInfoForSpinner
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadUserHomeInfoManager private constructor(private val context: Context) {
    companion object {
        private const val TAG = "LoadUserHomeInfoManager"
        private var INSTANCE: LoadUserHomeInfoManager? = null

        fun getInstance(context: Context): LoadUserHomeInfoManager {
            if (INSTANCE == null) {
                INSTANCE = LoadUserHomeInfoManager(context.applicationContext)
            }
            return INSTANCE as LoadUserHomeInfoManager
        }
    }

    interface OnLoadInfoCompleteListener {
        fun onSuccess(loadedItems: MutableList<String>)
        fun onError(message: String)
    }

    fun itemDelete(onLoadInfoCompleteListener: OnLoadInfoCompleteListener) {
        val api = RetrofitManager.getUserHomeInfoLoadApiService()

        val call = api.loadUserHomeInfo(App.prefs.getString("userid", ""))

        call.enqueue(object : Callback<UserHomeInfoResponse> {
            override fun onResponse(call: Call<UserHomeInfoResponse>, response: Response<UserHomeInfoResponse>) {
                if (response.body() != null) {
                    try {
                        val loadedItems = LoadInfoForSpinner.userHomeInfoLoadResponse(response.body()!!)
                        onLoadInfoCompleteListener.onSuccess(loadedItems)
                    } catch (e: JSONException) {
                        onLoadInfoCompleteListener.onError("응답 결과 파싱 중 오류가 발생했습니다")
                    }
                }
            }
            override fun onFailure(call: Call<UserHomeInfoResponse>, t: Throwable) {
                onLoadInfoCompleteListener.onError("통신 실패")
                call.cancel()
            }
        })
    }
}