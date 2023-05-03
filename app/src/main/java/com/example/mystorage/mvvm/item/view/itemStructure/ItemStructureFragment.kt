package com.example.mystorage.mvvm.item.view.itemStructure

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystorage.MainPage
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentItemStructureBinding
import com.example.mystorage.retrofit.response.UserItem
import com.example.mystorage.retrofit.response.UserItemResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.adapter.StrAdapter
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemStructureFragment : Fragment(), ItemStructureIView, View.OnClickListener {
    private lateinit var binding: FragmentItemStructureBinding
    private var itemList = mutableListOf<List<UserItem>>()
    private var placeList = mutableListOf<String>().apply { add("전체") }
    private val adapter= StrAdapter(itemList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "ItemStructureFragment - onCreateView() called")
        binding = FragmentItemStructureBinding.inflate(inflater, container, false)
        setRecyclerView()

        binding.menuBtnInStr.setOnClickListener(this)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        itemList.clear()
        placeList.clear()
        placeList.add("전체")
        getResponseOnItemLoad()
    }

    override fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

    }

    override fun setSearchSpinner(data : Map<String?, List<UserItem>>) {
        val spinner = binding.searchSpinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, placeList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterItemList(data, placeList[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun filterItemList(data : Map<String?, List<UserItem>>, searchPlace: String) {
        val filteredList = mutableListOf<List<UserItem>>()

        if (searchPlace == "전체") {
            adapter.updateItemList(itemList)
        } else {
            filteredList.add(data[searchPlace]!!.toList())
            adapter.updateItemList(filteredList)
        }

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
                        adapter.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        Log.d(TAG, "ItemStructureFragment - getResponseOnItemLoad() - onResponse() 응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }

            override fun onFailure(call: Call<UserItemResponse>, t: Throwable) {
                Log.d(TAG, "ItemStructureFragment - getResponseOnItemLoad() - onFailure() 통신 실패")
                call.cancel()
            }
        })
    }

    override fun itemLoadResponse(response: UserItemResponse) {
        if (response.status == "true") {
            if (!response.data.isNullOrEmpty()) {
                response.data.groupBy { it.itemplace }.map {
                    itemList.add(it.value.toList())
                    placeList.add(it.key.toString())
                }
                // 검색 스피너 설정
                setSearchSpinner(response.data.groupBy { it.itemplace })
            }
        } else {
            Log.d(TAG, "ItemStructureFragment - itemLoadResponse() ${response.message}")
        }
    }

    override fun onItemListSuccess(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        parentFragmentManager.beginTransaction()
            .replace(R.id.view_pager, ItemStructureFragment())
            .commit()
    }

    override fun onItemListError(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.menuBtnInStr -> {
                (activity as? MainPage)?.showDrawer()
            }
        }
    }
}