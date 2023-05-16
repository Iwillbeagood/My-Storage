package com.example.mystorage.retrofit.retrofitManager

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.mystorage.mvvm.item.model.ocr.OcrResponse
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*
import android.util.Base64
import com.example.mystorage.mvvm.item.model.ocr.ImageX
import com.example.mystorage.mvvm.item.model.ocr.OcrRequest
import com.example.mystorage.utils.Constants

class NaverOcrManager private constructor(private val context: Context) {
    companion object {
        private const val TAG = "NaverOcrManager"
        const val secretKey = "VHpMWmhHTHR1S2ZiblVSWW1BU3FTdkdMUUxMQXNubkg="
        private var INSTANCE: NaverOcrManager? = null

        fun getInstance(context: Context): NaverOcrManager {
            if (INSTANCE == null) {
                INSTANCE = NaverOcrManager(context.applicationContext)
            }
            return INSTANCE as NaverOcrManager
        }
    }

    interface OnOcrCompleteListener {
        fun onSuccess(ocrResult: MutableList<String>)
        fun onError(message: String)
    }


    fun ocrImage(bitmap: Bitmap, onOcrCompleteListener: OnOcrCompleteListener) {
        Log.d(Constants.TAG, "NaverOcrManager - ocrImage() called")
        val api = NaverOcrRetrofitManager.getNaverOcrMApiService()

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)

        val ocrRequest = OcrRequest(
            version = "V2",
            requestId = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis().toInt(),
            lang = "ko",
            images = listOf(
                ImageX(
                    format = "jpg",
                    name = "receipt",
                    data = base64Image
                )
            )
        )

        val call = api.ocrGeneral(
            secretKey,
            ocrRequest)

        call.enqueue(object : Callback<OcrResponse> {
            override fun onResponse(call: Call<OcrResponse>, response: Response<OcrResponse>) {
                if (response.isSuccessful) {
                    try {
                        Log.d(Constants.TAG, "NaverOcrManager - onResponse() called")
                        responseOnOcrImage(response.body()!!, onOcrCompleteListener)
                    } catch (e: JSONException) {
                        onOcrCompleteListener.onError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }

            override fun onFailure(call: Call<OcrResponse>, t: Throwable) {
                onOcrCompleteListener.onError("통신 실패")
                call.cancel()
            }
        })
    }

    fun responseOnOcrImage(ocrResponse: OcrResponse, onOcrCompleteListener: OnOcrCompleteListener) {
        Log.d(Constants.TAG, "AutoItemAddFragment - responseOnOcrImage() called")
        val result = ocrResponse.images.first()
        val ocrResult = mutableListOf<String>()

        if (result.inferResult != "SUCCESS" || result.message != "SUCCESS") {
            onOcrCompleteListener.onError("이미지 인식에 실패했습니다")
            return
        }

        var line = ""
        result.fields.forEach {
            line += "${it.inferText} "
            if (it.lineBreak) {
                ocrResult.add(line)
                line = ""
            }
        }

        onOcrCompleteListener.onSuccess(ocrResult)
    }
}