package com.example.mystorage.retrofit.retrofitManager

import android.content.Context
import com.example.mystorage.retrofit.model.naverSMS.Message
import com.example.mystorage.retrofit.model.naverSMS.SMSRequest
import com.example.mystorage.retrofit.model.naverSMS.SMSResponse
import com.example.mystorage.utils.Constants
import com.example.mystorage.utils.CustomToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONException

class NaverSMSManager private constructor(private val context: Context) {
    companion object {
        private const val TAG = "NaverSMSManager"
        private var INSTANCE: NaverSMSManager? = null

        fun getInstance(context: Context): NaverSMSManager {
            if (INSTANCE == null) {
                INSTANCE = NaverSMSManager(context.applicationContext)
            }
            return INSTANCE as NaverSMSManager
        }
    }

    interface OnSMSCompleteListener {
        fun onSuccess()
        fun onError(message: String)
    }

    fun sendSMS(to: String, authenticationNum: Int, onSMSCompleteListener: OnSMSCompleteListener) {
        val smsApi = NaverSMSRetrofitManager.getNaverSMSMApiService()
        val smsRequest = SMSRequest(
            type = "SMS",
            contentType = "COMM",
            countryCode = "82",
            from = "01099055272",
            content = "message",
            messages = listOf(
                Message(
                    content = "[MyStorage] 인증번호: $authenticationNum 인증번호를 입력해 주세요.",
                    to = to
                )
            )
        )

        val call = smsApi.sendSms(
            Constants.SERVICE_ID_NAVER,
            smsRequest
        )
        call.enqueue(object : Callback<SMSResponse> {
            override fun onResponse(call: Call<SMSResponse>, response: Response<SMSResponse>) {
                if (response.isSuccessful && response.body()!!.statusName == "success") {
                    try {
                        CustomToast.createToast(context, "$to 로 메세지를 성공적으로 발송했습니다.")
                        onSMSCompleteListener.onSuccess()
                    } catch (e: JSONException) {
                        onSMSCompleteListener.onError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }

            override fun onFailure(call: Call<SMSResponse>, t: Throwable) {
                onSMSCompleteListener.onError("통신 실패")
                call.cancel()
            }
        })
    }
}