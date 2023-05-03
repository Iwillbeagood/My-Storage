package com.example.mystorage.mvvm.item.view.itemList

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystorage.MainPage
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentItemListBinding
import com.example.mystorage.mvvm.item.view.itemUpdate.ItemUpdateFragment
import com.example.mystorage.retrofit.response.UserItem
import com.example.mystorage.retrofit.response.UserItemResponse
import com.example.mystorage.retrofit.response.ApiResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.DialogUtils
import com.example.mystorage.utils.adapter.ListGridAdapter
import com.example.mystorage.utils.GridSpacingItemDecoration
import com.example.mystorage.utils.TextWatcherUtil.createTextWatcher
import kotlinx.serialization.json.Json
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemListFragment : Fragment(), ItemListIView, View.OnClickListener {
    private lateinit var binding: FragmentItemListBinding
    private var itemList = mutableListOf<UserItem>()
    private val gridAdapter= ListGridAdapter(itemList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "ItemListFragment - onCreateView() called")
        binding = FragmentItemListBinding.inflate(inflater, container, false)
        setGridRecyclerView()

        binding.searchEditInList.addTextChangedListener(createTextWatcher { searchText ->
            filterItemList(searchText)
        })

        binding.menuBtnInList.setOnClickListener(this)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        itemList.clear()
        getResponseOnItemLoad()
    }

    override fun filterItemList(searchText: String) {
        val filteredList = mutableListOf<UserItem>()
        for (item in itemList) {
            if (item.itemname?.contains(searchText, ignoreCase = true) == true) {
                filteredList.add(item)
            }
        }
        gridAdapter.updateItemList(filteredList)
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

        gridAdapter.setItemClickListener(object : ListGridAdapter.OnItemClickListener {
            private var lastClickTime: Long = 0
            override fun onItemClick(v: View, position: Int) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    // 1초 이내 중복 클릭 시 아무런 작업을 하지 않음
                    return
                }
                lastClickTime = SystemClock.elapsedRealtime()

                DialogUtils.showNoMessageDialog(
                    requireActivity(),
                    "",
                    "정보 수정",
                    "물건 삭제",
                    onPositiveClick = {
                        val itemUpdateFragment = ItemUpdateFragment()
                        val bundle = Bundle()
                        val userItem : UserItem = itemList[position]
                        val json = Json.encodeToString(UserItem.serializer(), userItem)
                        bundle.putString("userItem", json)

                        itemUpdateFragment.arguments = bundle
                        itemUpdateFragment.show(parentFragmentManager, "ItemUpdateFragment")
                    },
                    onNegativeClick = {
                        getResponseOnDelete(itemList[position].itemname.toString(),
                            itemList[position].itemimage.toString())
                    }
                )
            }
        })
    }

    override fun getResponseOnItemLoad() {
        val api = RetrofitManager.getItemLoadApiService()

        val call = api.loadItem(
            App.prefs.getString("userid", "")
        )

        call.enqueue(object : Callback<UserItemResponse> {
            override fun onResponse(call: Call<UserItemResponse>, response: Response<UserItemResponse>) {
                if (response.body() != null) {
                    try {
                        itemLoadResponse(response.body()!!)
                        gridAdapter.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        Log.d(TAG, "ItemListFragment - getResponseOnItemLoad() - onResponse() 응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }

            override fun onFailure(call: Call<UserItemResponse>, t: Throwable) {
                Log.d(TAG, "ItemListFragment - getResponseOnItemLoad() - onFailure() 통신 실패")
                call.cancel()
            }
        })
    }

    override fun getResponseOnDelete(itemname: String, itemimage: String) {
        val api = RetrofitManager.getItemDeleteApiService()

        val call = api.deleteItem(
            App.prefs.getString("userid", ""),
            itemname,
            itemimage
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        itemDeleteResponse(response.body()!!)
                    } catch (e: JSONException) {
                        Log.d(TAG, "ItemListFragment - getResponseOnDelete() - onResponse() 응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d(TAG, "ItemListFragment - getResponseOnDelete() - onFailure() 통신 실패")
                call.cancel()
            }

        })
    }

    override fun itemLoadResponse(response: UserItemResponse) {
        if (response.status == "true") {
            if (!response.data.isNullOrEmpty()) {
                itemList.addAll(response.data)
            }
        } else {
            Log.d(TAG, "ItemListFragment - itemLoadResponse() ${response.message}")
        }
    }

    override fun itemDeleteResponse(response: ApiResponse) {
        val message = response.message
        Log.d(TAG, "ItemListFragment - deleteResponse() $message")
        if (response.status == "true") {
            onItemListSuccess(message)
        } else {
            onItemListError(message)
        }
    }

    override fun onItemListSuccess(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        onResume()
    }

    override fun onItemListError(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.menuBtnInList -> {
                (activity as? MainPage)?.showDrawer()
            }
        }
    }
}