package com.example.mystorage.mvvm.item.viewmodel.itemAdd

import android.graphics.Bitmap
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mystorage.mvvm.item.model.Item
import com.example.mystorage.mvvm.item.view.itemAdd.ItemAddDialogIView
import com.example.mystorage.retrofit.response.ApiResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.App
import com.example.mystorage.utils.BitmapConverter
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.DecodeFileUtil
import com.example.mystorage.utils.TextWatcherUtil.createTextWatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class ItemAddViewModel(private val itemAddDialogIView: ItemAddDialogIView): ViewModel(), ItemAddIViewModel {
    private val item: Item = Item("", Pair("", ""), "", "", "1", "", "")

    val itemnameTextWatcher: TextWatcher = createTextWatcher(item::setItemname)
    val itemstoreTextWatcher: TextWatcher = createTextWatcher(item::setItemstore)
    val itemcountTextWatcher: TextWatcher = createTextWatcher(item::setItemcount)

    override fun setItemPlace(itemplace: String) {
        item.setItemplace(itemplace)
    }

    override fun setItemImage(type: String, itemImage: String) {
        item.setItemimage(Pair(type, itemImage))
    }

    override fun onItemAdd(type: String) {
        if (!item.itemIsValid().first) {
            itemAddDialogIView.onItemAddSuccess(item.itemIsValid().second)
        } else {
            if (type == "add") getResponseOnItemAdd()
            else if (type == "countUpdate") getResponseOnItemCountUpdate()
        }
    }

    override fun getResponseOnItemAdd() {
        Log.d(TAG, "ItemAddViewModel - getResponseOnItemAdd() called")
        val api = RetrofitManager.getItemAddApiService()
        val bitmap: Bitmap?
        val file: File?
        val stream = ByteArrayOutputStream()
        var quality = 100

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

        // 이미지 크기가 2MB 보다 큰 경우
        while (bitmap != null && stream.toByteArray().size / 1024 > 2048) {
            quality -= 10
            stream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        }

        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
        val itemimage = MultipartBody.Part.createFormData("itemimage", "image.jpg", requestBody)

        val call = api.addItem(
            App.prefs.getString("userid", "").toRequestBody("text/plain".toMediaTypeOrNull()),
            item.getItemname().toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            itemimage,
            item.getItemplace().toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            item.getItemstore().toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            item.getItemcount().toString().toRequestBody("text/plain".toMediaTypeOrNull())
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        itemResponse(response.body()!!)
                    } catch (e: JSONException) {
                        itemAddDialogIView.onItemAddError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                itemAddDialogIView.onItemAddError("통신 실패")
                call.cancel()
            }
        })
    }

    override fun getResponseOnItemCountUpdate() {
        Log.d(TAG, "ItemAddViewModel - getResponseOnItemUpdate() called")
        // 아이템의 이름이 중복된다면 이미 존재하는 아이템의 개수를 입력한 개수만큼 증가
        val api = RetrofitManager.getItemCountUpdateApiService()

        val call = api.updateItemCount(
            App.prefs.getString("userid", ""),
            item.getItemname().toString(),
            item.getItemcount().toString().toInt()
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        itemResponse(response.body()!!)
                    } catch (e: JSONException) {
                        itemAddDialogIView.onItemAddError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                itemAddDialogIView.onItemAddError("통신 실패")
                call.cancel()
            }
        })
    }

    override fun itemResponse(response: ApiResponse) {
        val message = response.message
        Log.d(TAG, "ItemAddViewModel - itemResponse() called - response: $message")
        when (response.status) {
            "true" -> {
                itemAddDialogIView.onItemAddSuccess(message)
            }
            "false" -> {
                itemAddDialogIView.onItemAddError(message)
            }
            else -> {
                itemAddDialogIView.onItemCountUpdate(message.replace("\\n", "\n"))
            }
        }
    }
}