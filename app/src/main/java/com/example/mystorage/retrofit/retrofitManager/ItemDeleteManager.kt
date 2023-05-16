package com.example.mystorage.retrofit.retrofitManager

import android.content.Context
import android.util.Log
import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.retrofit.model.naverSMS.Message
import com.example.mystorage.retrofit.model.naverSMS.SMSRequest
import com.example.mystorage.retrofit.model.naverSMS.SMSResponse
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants
import com.example.mystorage.utils.CustomToast
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemDeleteManager private constructor(private val context: Context) {
    companion object {
        private const val TAG = "ItemDeleteManager"
        private var INSTANCE: ItemDeleteManager? = null

        fun getInstance(context: Context): ItemDeleteManager {
            if (INSTANCE == null) {
                INSTANCE = ItemDeleteManager(context.applicationContext)
            }
            return INSTANCE as ItemDeleteManager
        }
    }

    interface OnItemDeleteCompleteListener {
        fun onSuccess(response: ApiResponse)
        fun onError(message: String)
    }

    fun itemDelete(itemid: Int, itemimage: String, onItemDeleteCompleteListener: OnItemDeleteCompleteListener) {
        val api = RetrofitManager.getItemDeleteApiService()

        val call = api.deleteItem(
            App.prefs.getString("userid", ""),
            itemid,
            itemimage,
            "items"
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        onItemDeleteCompleteListener.onSuccess(response.body()!!)
                    } catch (e: JSONException) {
                        onItemDeleteCompleteListener.onError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                onItemDeleteCompleteListener.onError("통신 실패")
                call.cancel()
            }

        })
    }
}