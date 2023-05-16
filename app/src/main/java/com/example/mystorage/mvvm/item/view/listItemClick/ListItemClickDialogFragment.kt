package com.example.mystorage.mvvm.item.view.listItemClick

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentListItemClickDialogBinding
import com.example.mystorage.mvvm.item.view.itemList.ItemListFragment
import com.example.mystorage.mvvm.item.view.itemUpdate.ItemUpdateFragment
import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.retrofit.model.UserItem
import com.example.mystorage.retrofit.retrofitManager.ItemDeleteManager
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.CustomToast
import kotlinx.serialization.json.Json
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListItemClickDialogFragment : DialogFragment(), ListItemClickIView, View.OnClickListener {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    lateinit var binding: FragmentListItemClickDialogBinding

    lateinit var userItem: UserItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "ListItemClickDialogFragment - onCreateView() called")
        binding = FragmentListItemClickDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val json = bundle?.getString("userItem")
        userItem = Json.decodeFromString(UserItem.serializer(), json!!)

        binding.useItemView.setOnClickListener(this)
        binding.deleteItemView.setOnClickListener(this)
        binding.updateInfoView.setOnClickListener(this)
        binding.minusCountView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.useItemView -> {
                if (binding.useItemView.isEnabled) {
                    binding.useItemView.isEnabled = false
                    Log.d(TAG, "ListItemClickDialogFragment - useItemView onClick() called")
                    getResponseOnUsedItemMove()
                    binding.useItemView.isEnabled = true
                }
            }
            R.id.minusCountView -> {
                if (binding.minusCountView.isEnabled) {
                    binding.minusCountView.isEnabled = false
                    Log.d(TAG, "ListItemClickDialogFragment - minusCountView onClick() called")
                    if (userItem.itemcount!!.toInt() >= 2)
                        getResponseOnItemCountMinus()
                    else {
                        onItemError("물건이 하나밖에 남지가 않았습니다")
                        dismiss()
                    }

                    binding.minusCountView.isEnabled = true
                }
            }
            R.id.deleteItemView -> {
                if (binding.deleteItemView.isEnabled) {
                    binding.deleteItemView.isEnabled = false
                    Log.d(TAG, "ListItemClickDialogFragment - onClick() called")
                    getResponseOnDelete(userItem.itemid, userItem.itemimage.toString())
                    binding.deleteItemView.isEnabled = true
                }
            }
            R.id.updateInfoView -> {
                if (binding.updateInfoView.isEnabled) {
                    binding.updateInfoView.isEnabled = false
                    Log.d(TAG, "ListItemClickDialogFragment - onClick() called")
                    dismiss()
                    val itemUpdateFragment = ItemUpdateFragment()
                    val bundle = Bundle()
                    val json = Json.encodeToString(UserItem.serializer(), userItem)
                    bundle.putString("userItem", json)

                    itemUpdateFragment.arguments = bundle
                    itemUpdateFragment.show(parentFragmentManager, "ItemUpdateFragment")
                    binding.updateInfoView.isEnabled = true
                }
            }
        }
    }

    override fun getResponseOnDelete(itemid: Int, itemimage: String) {
        val itemDeleteManager = ItemDeleteManager.getInstance(requireContext())

        itemDeleteManager.itemDelete(
            itemid,
            itemimage,
            object : ItemDeleteManager.OnItemDeleteCompleteListener {
                override fun onSuccess(response: ApiResponse) {
                    itemResponse(response)
                }
                override fun onError(message: String) {
                    onItemError(message)
                }

            }
        )
    }

    override fun getResponseOnItemCountMinus() {
        Log.d(TAG, "ListItemClickDialogFragment - getResponseOnItemCountMinus() called")
        val api = RetrofitManager.getItemCountUpdateApiService()

        val call = api.updateItemCount(
            App.prefs.getString("userid", ""),
            userItem.itemid,
            -1
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        itemResponse(response.body()!!)
                    } catch (e: JSONException) {
                        onItemError("응답 결과 파싱 중 오류가 발생했습니다")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                onItemError("통신 실패")
                call.cancel()
            }
        })
    }

    override fun getResponseOnUsedItemMove() {
        Log.d(TAG, "ListItemClickDialogFragment - getResponseOnUsedItemMove() called")
        val api = RetrofitManager.getItemMoveApiService()

        val call = api.usedItemMove(
            App.prefs.getString("userid", ""),
            userItem.itemid
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        itemResponse(response.body()!!)
                    } catch (e: JSONException) {
                        onItemError("응답 결과 파싱 중 오류가 발생했습니다")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                onItemError("통신 실패")
                call.cancel()
            }
        })
    }

    override fun itemResponse(response: ApiResponse) {
        val message = response.message
        Log.d(TAG, "ListItemClickDialogFragment - itemResponse() called - response: $message")
        when (response.status) {
            "true" -> {
                onItemSuccess(message)
            }
            "false" -> {
                onItemError(message)
            }
        }
    }

    override fun onItemSuccess(message: String?) {
        Log.d(TAG, "ListItemClickDialogFragment - onItemSuccess() called")
        CustomToast.createToast(requireActivity(), message.toString()).show()
        dismiss()
    }

    override fun onItemError(message: String?) {
        Log.d(TAG, "ListItemClickDialogFragment - onItemError() called")
        CustomToast.createToast(requireActivity(), message.toString()).show()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val itemListFragment = parentFragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":1") as ItemListFragment?
        itemListFragment?.onResume() // itemListFragment 인스턴스를 가져와서 getResponseOnItemLoad 메소드 호출
    }
}