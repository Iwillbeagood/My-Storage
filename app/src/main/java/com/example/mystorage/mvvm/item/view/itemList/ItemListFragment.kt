package com.example.mystorage.mvvm.item.view.itemList

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystorage.MainPage
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentItemListBinding
import com.example.mystorage.mvvm.item.view.listItemClick.ListItemClickDialogFragment
import com.example.mystorage.retrofit.model.UserItem
import com.example.mystorage.retrofit.model.UserItemResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.CustomToast
import com.example.mystorage.adapter.ListGridAdapter
import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.retrofit.retrofitManager.ItemDeleteManager
import com.example.mystorage.retrofit.retrofitManager.LoadUserHomeInfoManager
import com.example.mystorage.utils.DialogUtils
import com.example.mystorage.utils.GridSpacingItemDecoration
import com.example.mystorage.utils.TextWatcherUtil.createTextWatcher
import kotlinx.serialization.json.Json
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemListFragment : Fragment(), ItemListIView, View.OnClickListener {
    private lateinit var binding: FragmentItemListBinding
    private var itemList = mutableListOf<Pair<UserItem, Boolean>>()
    private val gridAdapter= ListGridAdapter(itemList)
    var items = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "ItemListFragment - onCreateView() called")
        binding = FragmentItemListBinding.inflate(inflater, container, false)
        setGridRecyclerView()

        // 검색어로 아이템 찾기
        binding.searchEditInList.addTextChangedListener(createTextWatcher { searchText ->
            filterItemList(searchText)
        })

        // 전체를 선택하면 아이템 전체 선택
        binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                itemList = itemList.map { it.copy(second = true) }.toMutableList()
                gridAdapter.updateItemList(itemList)
            } else {
                itemList = itemList.map { it.copy(second = false) }.toMutableList()
                gridAdapter.updateItemList(itemList)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            Log.d(TAG, "ItemListFragment - onBackPressedDispatcher() called")
            exitLongClickMode()
        }

        binding.menuBtnInList.setOnClickListener(this)
        binding.deleteSelectedItem.setOnClickListener(this)
        binding.moveSelectedItem.setOnClickListener(this)
        binding.exitLongClickMode.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.tag = "list_item_fragment_tag"



        // 장소 선택을 위한 스피너 설정
        val spinner = binding.searchSpinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // 스피너 아이템을 서버로부터 불러와서 설정
        val loadInfoManager = LoadUserHomeInfoManager.getInstance(requireContext())

        loadInfoManager.itemDelete(
            object : LoadUserHomeInfoManager.OnLoadInfoCompleteListener {
                override fun onSuccess(loadedItems: MutableList<String>) {
                    items.addAll(loadedItems)
                    adapter.notifyDataSetChanged()
                }

                override fun onError(message: String) {
                    onItemListError(message)
                }

            }
        )
    }

    override fun filterItemList(searchText: String) {
        val filteredList = mutableListOf<Pair<UserItem, Boolean>>()
        for (item in itemList) {
            if (item.first.itemname?.contains(searchText, ignoreCase = true) == true) {
                filteredList.add(item)
            }
        }
        gridAdapter.updateItemList(filteredList)
    }

    private var longClickCheck = false
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
                // 롱클릭일 때 이벤트가 발생하고 있기 때문에 롱클릭 동안에는 아이템 일반클릭시 아이템 선택으로 바꿈
                if (longClickCheck) {
                    updateItemCheck(position)
                } else {
                    // 일반 클릭일 때는 아이템에 대한 설정 다이얼로그로 이동
                    val listItemClickDialogFragment = ListItemClickDialogFragment()
                    val bundle = Bundle()
                    val userItem : UserItem = itemList[position].first
                    val json = Json.encodeToString(UserItem.serializer(), userItem)
                    bundle.putString("userItem", json)

                    listItemClickDialogFragment.arguments = bundle
                    listItemClickDialogFragment.show(parentFragmentManager, "ListItemClickDialogFragment")

                    lastClickTime = SystemClock.elapsedRealtime()
                }
            }
        })

        gridAdapter.setItemLongClickListener(object : ListGridAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int) {
                if (!longClickCheck) {
                    longClickCheck = true
                    gridAdapter.isLongClickMode = true
                    binding.longClickMode1.visibility = View.VISIBLE
                    // 클릭시 진동 이벤트
                    val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    val milliseconds = 20L
                    vibrator.vibrate(milliseconds)

                    updateItemCheck(position)
                } else {
                    if (!itemList[position].second)
                        exitLongClickMode()
                }
            }
        })
    }

    fun updateItemCheck(position: Int) {
        val isChecked = !itemList[position].second
        itemList[position] = itemList[position].copy(second = isChecked)
        gridAdapter.updateItemList(itemList)
    }

    fun exitLongClickMode() {
        longClickCheck = false
        gridAdapter.isLongClickMode = false
        binding.longClickMode1.visibility = View.GONE
        itemList = itemList.map { it.copy(second = false) } as MutableList<Pair<UserItem, Boolean>>
        gridAdapter.updateItemList(itemList)

        onResume()
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

    override fun itemLoadResponse(response: UserItemResponse) {
        itemList.clear()
        if (response.status == "true") {
            if (!response.data.isNullOrEmpty()) {
                for (item in response.data) {
                    itemList.add(Pair(item, false))
                }
                gridAdapter.updateItemList(itemList)
                binding.emptyHint.visibility = View.INVISIBLE
            } else {
                binding.emptyHint.visibility = View.VISIBLE
            }
        } else {
            Log.d(TAG, "ItemListFragment - itemLoadResponse() ${response.message}")
        }
    }

    override fun onItemListSuccess(message: String?) {
        CustomToast.createToast(requireActivity(), message.toString()).show()
        exitLongClickMode()
    }

    override fun onItemListError(message: String?) {
        CustomToast.createToast(requireActivity(), message.toString()).show()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.menuBtnInList -> {
                if (binding.menuBtnInList.isEnabled) {
                    binding.menuBtnInList.isEnabled = false
                    Log.d(TAG, "ItemListFragment - menuBtnInList onClick() called")
                    (activity as? MainPage)?.showDrawer()
                    binding.menuBtnInList.isEnabled = true
                }
            }
            R.id.moveSelectedItem -> {
                if (binding.moveSelectedItem.isEnabled) {
                    binding.moveSelectedItem.isEnabled = false
                    Log.d(TAG, "ItemListFragment - moveSelectedItem onClick() called")
                    val selectItem = binding.searchSpinner.selectedItem.toString()
                    DialogUtils.showDialog(
                        requireActivity(),
                        "${selectItem}으로 선택된 물건을 이동합니다",
                        "확인",
                        "취소",
                        onPositiveClick = {
                            updateItemplace(selectItem)
                        },
                        onNegativeClick = {
                        }
                    )
                    binding.moveSelectedItem.isEnabled = true
                }
            }
            R.id.deleteSelectedItem -> {
                if (binding.deleteSelectedItem.isEnabled) {
                    binding.deleteSelectedItem.isEnabled = false
                    Log.d(TAG, "ItemListFragment - deleteSelectedItem onClick() called")
                    DialogUtils.showDialog(
                        requireActivity(),
                       "선택된 물건을 삭제합니다",
                        "확인",
                        "취소",
                        onPositiveClick = {
                            deleteSelectedItem()
                        },
                        onNegativeClick = {
                        }
                    )
                    binding.deleteSelectedItem.isEnabled = true
                }
            }
            R.id.exitLongClickMode -> {
                if (binding.exitLongClickMode.isEnabled) {
                    binding.exitLongClickMode.isEnabled = false
                    Log.d(TAG, "ItemListFragment - exitLongClickMode onClick() called")
                    exitLongClickMode()
                    binding.exitLongClickMode.isEnabled = true
                }
            }
        }
    }

    override fun deleteSelectedItem() {
        val selectedItem = itemList.filter { it.second }

        selectedItem.map {
            getResponseOnDelete(it.first.itemid, it.first.itemimage.toString())
        }
    }

    override fun getResponseOnDelete(itemid: Int, itemimage: String) {
        Log.d(TAG, "ItemListFragment - getResponseOnDelete() called")
        val itemDeleteManager = ItemDeleteManager.getInstance(requireContext())

        itemDeleteManager.itemDelete(
            itemid,
            itemimage,
            object : ItemDeleteManager.OnItemDeleteCompleteListener {
                override fun onSuccess(response: ApiResponse) {
                    responseOnItem(response)
                }
                override fun onError(message: String) {
                    onItemListError(message)
                }
            }
        )
    }

    override fun updateItemplace(itemplace: String) {
        val selectedItem = itemList.filter { it.second }

        selectedItem.map {
            getResponseOnUpdateItemplace(it.first.itemid, itemplace)
        }
    }

    override fun getResponseOnUpdateItemplace(itemid: Int, itemplace: String) {
        Log.d(TAG, "ItemListFragment - getResponseOnUpdateItem() called")
        val api = RetrofitManager.getItemPlaceUpdateApiService()

        val call = api.itemPlaceUpdate(
            App.prefs.getString("userid", ""),
            itemid,
            itemplace
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        responseOnItem(response.body()!!)
                    } catch (e: JSONException) {
                        onItemListError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                onItemListError("통신 실패")
                call.cancel()
            }

        })
    }

    override fun responseOnItem(response: ApiResponse) {
        val message = response.message
        when (response.status) {
            "true" -> {
                onItemListSuccess(message)
            }
            "false" -> {
                onItemListError(message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "ItemListFragment - onResume() called")
        getResponseOnItemLoad()
    }

}
