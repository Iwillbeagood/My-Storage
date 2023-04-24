package com.example.mystorage.mvvm.item.viewmodel

import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mystorage.mvvm.item.model.Item
import com.example.mystorage.mvvm.item.view.itemAddView.ItemAddDialogIView
import com.example.mystorage.retrofit.response.ApiResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.ImageUtils
import com.example.mystorage.utils.ImageUtils.getRealPathFromURI
import com.example.mystorage.utils.TextWatcherUtil.createTextWatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class ItemAddViewModel(private val itemAddDialogIView: ItemAddDialogIView): ViewModel(), ItemAddIViewModel {
    private val item: Item = Item("", "", "", "", "1")

    val itemnameTextWatcher: TextWatcher = createTextWatcher(item::setItemname)
    val itemstoreTextWatcher: TextWatcher = createTextWatcher(item::setItemstore)
    val itemcountTextWatcher: TextWatcher = createTextWatcher(item::setItemcount)

    override fun setItemPlace(itemplace: String) {
        item.setItemplace(itemplace)
    }

    override fun setItemImage(itemImage: String) {
        item.setItemimage(itemImage)
    }

    override fun onItemAdd() {
        if (!item.itemIsValid().first) {
            itemAddDialogIView.onItemAddSuccess(item.itemIsValid().second)
        } else {
            Log.d(TAG, "ItemAddViewModel - 입력 오류 없음")
            getResponseOnItemAdd(true)
        }
    }

    override fun getResponseOnItemAdd(status: Boolean) {
        val call : Call<ApiResponse>

        if (status) {
            val api = RetrofitManager.getItemAddApiService()
            val file = File(item.getItemimage().toString())
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            // UUID 이용해 파일 이름 생성
            val filename = UUID.randomUUID().toString()
            // 생성된 파일 이름으로 MultipartBody.Part 객체 생성
            val itemimage = MultipartBody.Part.createFormData("itemimage", filename, requestBody)

            call = api.addItem(
                App.prefs.getString("userid", "").toRequestBody("text/plain".toMediaTypeOrNull()),
                item.getItemname().toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                itemimage,
                item.getItemplace().toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                item.getItemstore().toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                item.getItemcount().toString().toRequestBody("text/plain".toMediaTypeOrNull())
            )

        } else {
            // 아이템의 이름이 중복된다면 이미 존재하는 아이템의 개수를 입력한 개수만큼 증가
            val api = RetrofitManager.getItemCountUpdateApiService()

            call = api.updateItemCount(
                App.prefs.getString("userid", ""),
                item.getItemname().toString(),
                item.getItemcount().toString().toInt()
            )
        }

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