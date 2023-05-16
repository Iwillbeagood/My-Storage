package com.example.mystorage.mvvm.item.view.usedItemClick

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentUsedItemClickDialogBinding
import com.example.mystorage.mvvm.item.view.usedItem.UsedItemFragment
import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.retrofit.model.UserItem
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.CustomToast
import kotlinx.serialization.json.Json
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsedItemClickDialogFragment : DialogFragment(), UsedItemClickIView, View.OnClickListener {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private lateinit var binding: FragmentUsedItemClickDialogBinding

    lateinit var userItem: UserItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "UsedItemClickDialogFragment - onCreateView() called")
        binding = FragmentUsedItemClickDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val json = bundle?.getString("userItem")
        userItem = Json.decodeFromString(UserItem.serializer(), json!!)

        binding.usedItemMoveBackView.setOnClickListener(this)
        binding.deleteUsedItemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.usedItemMoveBackView -> {
                if (binding.usedItemMoveBackView.isEnabled) {
                    binding.usedItemMoveBackView.isEnabled = false
                    Log.d(TAG, "UsedItemClickDialogFragment - usedItemMoveBackView onClick() called")
                    getResponseOnUsedItemMove()
                    binding.usedItemMoveBackView.isEnabled = true
                }
            }
            R.id.deleteUsedItemView -> {
                if (binding.deleteUsedItemView.isEnabled) {
                    binding.deleteUsedItemView.isEnabled = false
                    Log.d(TAG, "UsedItemClickDialogFragment - deleteUsedItemView onClick() called")
                    getResponseOnUsedItemDelete()
                    binding.deleteUsedItemView.isEnabled = true
                }
            }
        }
    }
    override fun getResponseOnUsedItemDelete() {
        val api = RetrofitManager.getItemDeleteApiService()

        val call = api.deleteItem(
            App.prefs.getString("userid", ""),
            userItem.itemid,
            userItem.itemimage,
            "usedItems"
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        usedItemResponse(response.body()!!)
                    } catch (e: JSONException) {
                        Log.d(TAG, "UsedItemClickDialogFragment - getResponseOnUsedItemDelete() - onResponse() 응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "UsedItemClickDialogFragment - getResponseOnUsedItemDelete() - onFailure() 통신 실패")
                call.cancel()
            }

        })
    }

    override fun getResponseOnUsedItemMove() {
        val api = RetrofitManager.getUsedItemMoveApiService()

        val call = api.moveUsedItem(
            App.prefs.getString("userid", ""),
            userItem.itemid
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        usedItemResponse(response.body()!!)
                    } catch (e: JSONException) {
                        Log.d(TAG, "UsedItemClickDialogFragment - getResponseOnUsedItemMove() - onResponse() 응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "UsedItemClickDialogFragment - getResponseOnUsedItemMove() - onFailure() 통신 실패")
                call.cancel()
            }

        })
    }

    override fun usedItemResponse(response: ApiResponse) {
        val message = response.message
        Log.d(TAG, "UsedItemClickDialogFragment - usedItemResponse() $message")
        if (response.status == "true") {
            onUsedItemSuccess(message)
        } else {
            onUsedItemError(message)
        }
    }

    override fun onUsedItemSuccess(message: String?) {
        Log.d(TAG, "UsedItemClickDialogFragment - onUsedItemSuccess() called")
        CustomToast.createToast(requireActivity(), message.toString()).show()
        dismiss()
    }

    override fun onUsedItemError(message: String?) {
        Log.d(TAG, "ListItemClickDialogFragment - onItemError() called")
        CustomToast.createToast(requireActivity(), message.toString()).show()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val usedItemFragment = parentFragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":2") as UsedItemFragment?
        usedItemFragment?.onResume() // itemListFragment 인스턴스를 가져와서 getResponseOnItemLoad 메소드 호출
    }
}