package com.example.mystorage.mvvm.item.viewmodel.itemUpdate

import android.graphics.Bitmap
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mystorage.mvvm.item.model.Item
import com.example.mystorage.mvvm.item.view.itemUpdate.ItemUpdateIView
import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.*
import com.example.mystorage.utils.Constants.TAG
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class ItemUpdateViewModel(private val itemUpdateIView: ItemUpdateIView): ViewModel(), ItemUpdateIViewModel {
    private val item: Item = Item("", Pair("", ""), "", "", "1", "", "")

    val itemnameUpdateTextWatcher: TextWatcher = TextWatcherUtil.createTextWatcher(item::setItemname)
    val itemstoreUpdateTextWatcher: TextWatcher = TextWatcherUtil.createTextWatcher(item::setItemstore)
    val itemcountUpdateTextWatcher: TextWatcher = TextWatcherUtil.createTextWatcher(item::setItemcount)

    override fun setOriginItemInfo(itemname: String, itemstore: String, originname: String, originimage: String) {
        item.setItemname(itemname)
        item.setItemstore(itemstore)
        item.setOriginname(originname)
        item.setOriginimage(originimage)
    }

    override fun setItemPlace(itemplace: String) {
        item.setItemplace(itemplace)
    }

    override fun setItemImage(type: String, itemImage: String) {
        item.setItemimage(Pair(type, itemImage))
    }

    override fun onItemUpdate(itemid: Int) {
        if (!item.itemIsValid().first) {
            itemUpdateIView.onItemUpdateSuccess(item.itemIsValid().second)
        } else {
            getResponseOnItemUpdate(itemid)
        }
    }

    override fun getResponseOnItemUpdate(itemid: Int) {
        Log.d(TAG, "ItemUpdateViewModel - getResponseOnItemUpdate() called")
        val api = RetrofitManager.getItemUpdateApiService()
        val bitmap: Bitmap?
        val file: File?
        val stream = ByteArrayOutputStream()
        var quality = 100
        var itemimage: MultipartBody.Part? = null

        // 이미지를 선택한 방법에 따라 분기 처리
        when (item.getItemimage()?.first) {
            "bitmap" -> {
                bitmap = BitmapConverter().stringToBitmap(item.getItemimage()!!.second)
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            }
            "uri" -> {
                file = File(item.getItemimage()!!.second)
                bitmap = DecodeFileUtil.decodeFileWithOrientation(file)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            }
            else -> {
                bitmap = null
            }
        }

        if (bitmap != null) {
            // 이미지 크기가 2MB 보다 큰 경우
            while (stream.toByteArray().size / 1024 > 2048) {
                quality -= 10
                stream.reset()
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            }

            val byteArray = stream.toByteArray()
            val requestBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
            itemimage = MultipartBody.Part.createFormData("itemimage", "image.jpg", requestBody)
        } else {
            itemimage = null
        }

        val call = api.updateItem(
            App.prefs.getString("userid", "").toRequestBody("text/plain".toMediaTypeOrNull()),
            itemid.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            item.getItemname().toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            itemimage,
            item.getItemplace().toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            item.getItemstore().toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            item.getItemcount().toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            item.getOriginimage().toString().toRequestBody("text/plain".toMediaTypeOrNull())
        )
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        itemUpdateResponse(response.body()!!)
                    } catch (e: JSONException) {
                        itemUpdateIView.onItemUpdateError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                itemUpdateIView.onItemUpdateError("통신 실패")
                call.cancel()
            }
        })
    }

    override fun itemUpdateResponse(response: ApiResponse) {
        val message = response.message
        Log.d(TAG, "ItemUpdateViewModel - itemUpdateResponse() called - response: $message")
        when (response.status) {
            "true" -> {
                itemUpdateIView.onItemUpdateSuccess(message)
            }
            "false" -> {
                itemUpdateIView.onItemUpdateError(message)
            }
        }
    }
}