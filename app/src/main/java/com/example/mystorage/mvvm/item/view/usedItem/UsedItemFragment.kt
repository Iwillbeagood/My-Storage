package com.example.mystorage.mvvm.item.view.usedItem

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentUsedItemBinding
import com.example.mystorage.mvvm.item.view.usedItemClick.UsedItemClickDialogFragment
import com.example.mystorage.retrofit.model.UserItem
import com.example.mystorage.retrofit.model.UserItemResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.adapter.UsedItemGridAdapter
import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.utils.*
import kotlinx.serialization.json.Json
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsedItemFragment : Fragment(), UsedItemIView, View.OnClickListener {
    lateinit var binding: FragmentUsedItemBinding
    private var itemList = mutableListOf<UserItem>()
    private val gridAdapter= UsedItemGridAdapter(itemList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "UsedItemFragment - onCreateView() called")
        binding = FragmentUsedItemBinding.inflate(inflater, container, false)
        setGridRecyclerView()

        binding.clearUsedItemBtn.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.tag = "used_item_fragment_tag"
    }

    override fun setGridRecyclerView() {
        val spanCount = 2 // 그리드 열 수
        val spacing = 16 // 아이템 간의 간격 (px)

        // 그리드 레이아웃 매니저 설정
        val layoutManager = GridLayoutManager(context, spanCount)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.recyclerViewGrid.layoutManager = layoutManager
        binding.recyclerViewGrid.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, true))

        binding.recyclerViewGrid.adapter = gridAdapter

        gridAdapter.setUsedItemClickListener(object : UsedItemGridAdapter.OnItemClickListener {
            private var lastClickTime: Long = 0
            override fun onItemClick(v: View, position: Int) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    // 1초 이내 중복 클릭 시 아무런 작업을 하지 않음
                    return
                }
                Log.d(TAG, "UsedItemFragment - onItemClick() called")
                val usedItemClickDialogFragment = UsedItemClickDialogFragment()
                val bundle = Bundle()
                val userItem : UserItem = itemList[position]
                val json = Json.encodeToString(UserItem.serializer(), userItem)
                bundle.putString("userItem", json)

                usedItemClickDialogFragment.arguments = bundle
                usedItemClickDialogFragment.show(parentFragmentManager, "UsedItemClickDialogFragment")

                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.clear_used_item_btn -> {
                if (binding.clearUsedItemBtn.isEnabled) {
                    binding.clearUsedItemBtn.isEnabled = false
                    Log.d(TAG, "UsedItemFragment - clear_used_item_btn onClick() called")
                    DialogUtils.showNoMessageDialog(
                        requireContext(),
                        "물건을 사용 완료에서 모두 제거합니다",
                        "확인",
                        "취소",
                        onPositiveClick = {
                            // 창고에서 물건 제거
                            getResponseOnUsedItemDeleteAll()
                        },
                        onNegativeClick = {
                        }
                    )
                    binding.clearUsedItemBtn.isEnabled = true
                }
            }
        }
    }

    override fun getResponseOnUsedItemLoad() {
        val api = RetrofitManager.getUsedItemLoadApiService()

        val call = api.loadUsedItem(
            App.prefs.getString("userid", "")
        )

        call.enqueue(object : Callback<UserItemResponse> {
            override fun onResponse(call: Call<UserItemResponse>, response: Response<UserItemResponse>) {
                if (response.body() != null) {
                    try {
                        usedItemLoadResponse(response.body()!!)
                        gridAdapter.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        Log.d(TAG, "UsedItemFragment - getResponseOnUsedItemLoad() - onResponse() 응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }

            override fun onFailure(call: Call<UserItemResponse>, t: Throwable) {
                Log.d(TAG, "UsedItemFragment - getResponseOnUsedItemLoad() - onFailure() 통신 실패")
                call.cancel()
            }
        })
    }

    override fun getResponseOnUsedItemDeleteAll() {
        val api = RetrofitManager.getItemDeleteAllApiService()

        val call = api.deleteAllItem(
            App.prefs.getString("userid", ""),
            "usedItems"
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        deleteAllUsedItemResponse(response.body()!!)
                    } catch (e: JSONException) {
                        Log.d(TAG, "MainPage - getDeleteAllItem() - onResponse() 응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "MainPage - getDeleteAllItem() - onFailure() 통신 실패")
                call.cancel()
            }
        })
    }

    override fun usedItemLoadResponse(response: UserItemResponse) {
        if (response.status == "true") {
            if (!response.data.isNullOrEmpty()) {
                itemList.addAll(response.data)
                binding.emptyHint.visibility = View.INVISIBLE
            } else {
                binding.emptyHint.visibility = View.VISIBLE
            }
        } else {
            Log.d(TAG, "UsedItemFragment - usedItemLoadResponse() ${response.message}")
        }
    }

    override fun deleteAllUsedItemResponse(response: ApiResponse) {
        val message = response.message
        when (response.status) {
            "true" -> {
                onUsedItemLoadSuccess(message)
            }
            "false" -> {
                onUsedItemLoadError(message)
            }
        }
    }



    override fun onUsedItemLoadSuccess(message: String?) {
        Log.d(TAG, "UsedItemFragment - onUsedItemLoadSuccess() called")
        CustomToast.createToast(requireActivity(), message.toString()).show()
        onResume()
    }

    override fun onUsedItemLoadError(message: String?) {
        Log.d(TAG, "UsedItemFragment - onUsedItemLoadError() called")
        CustomToast.createToast(requireActivity(), message.toString()).show()
    }

    override fun onResume() {
        super.onResume()
        itemList.clear()
        getResponseOnUsedItemLoad()
    }
}