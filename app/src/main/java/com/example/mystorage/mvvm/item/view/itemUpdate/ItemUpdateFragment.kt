package com.example.mystorage.mvvm.item.view.itemUpdate

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mystorage.MainPage
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentItemUpdateBinding
import com.example.mystorage.mvvm.item.view.CameraGalleryDialogFragment
import com.example.mystorage.mvvm.item.view.itemList.ItemListFragment
import com.example.mystorage.mvvm.item.viewmodel.itemUpdate.ItemUpdateViewModel
import com.example.mystorage.mvvm.item.viewmodel.itemUpdate.ItemUpdateViewModelFactory
import com.example.mystorage.retrofit.response.UserHomeInfoResponse
import com.example.mystorage.retrofit.response.UserItem
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.*
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.ImageLoader.loadBitmap
import com.example.mystorage.utils.ImageLoader.loadUrl
import kotlinx.serialization.json.Json
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemUpdateFragment : DialogFragment(), ItemUpdateIView, View.OnClickListener {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
    }

    private lateinit var binding: FragmentItemUpdateBinding
    var items = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_update, container, false)
        val itemUpdateFactory = ItemUpdateViewModelFactory(this)
        binding.viewModel = ViewModelProvider(this, itemUpdateFactory)[ItemUpdateViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "ItemUpdateFragment - onViewCreated() called")
        binding.itemImageUpdate.setOnClickListener(this)
        binding.plusUpdateBtn.setOnClickListener(this)
        binding.minusUpdateBtn.setOnClickListener(this)
        binding.itemUpdateBtn.setOnClickListener(this)
        binding.itemUpdateBack.setOnClickListener(this)

        val bundle = arguments
        val json = bundle?.getString("userItem")
        val userItem = Json.decodeFromString(UserItem.serializer(), json!!)

        // 장소 선택을 위한 스피너 설정
        val spinner = binding.itemPlaceUpdateSpinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.viewModel!!.setItemPlace(items[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val api = RetrofitManager.getUserHomeInfoLoadApiService()
        val call = api.loadUserHomeInfo(App.prefs.getString("userid", ""))
        call.enqueue(object : Callback<UserHomeInfoResponse> {
            override fun onResponse(call: Call<UserHomeInfoResponse>, response: Response<UserHomeInfoResponse>) {
                if (response.body() != null) {
                    try {
                        val loadedItems = LoadInfoForSpinner.userHomeInfoLoadResponse(response.body()!!)
                        items.addAll(loadedItems)
                        adapter.notifyDataSetChanged()
                        // 사용자가 선택한 아이템으로 기본 설정
                        setUpdateSetting(userItem)
                    } catch (e: JSONException) {
                        onItemUpdateError("응답 결과 파싱 중 오류가 발생했습니다")
                    }
                }
            }
            override fun onFailure(call: Call<UserHomeInfoResponse>, t: Throwable) {
                onItemUpdateError("통신 실패")
                call.cancel()
            }
        })


    }

    override fun setUpdateSetting(userItem: UserItem) {
        binding.itemNameUpdateEdit.setText(userItem.itemname)
        binding.iteStoreUpdateEdit.setText(userItem.itemstore)
        binding.itemCountUpdateEdit.setText(userItem.itemcount)

        val index = items.indexOf(userItem.itemplace.toString())
        binding.itemPlaceUpdateSpinner.setSelection(index)

        binding.viewModel!!.setItemPlace(userItem.itemplace.toString())

        if (userItem.itemimage.isNullOrBlank()) {
            Log.d(TAG, "ListGridAdapter - onBindViewHolder() itemimage is null")
            binding.itemImageUpdateView.setImageResource(R.drawable.box)
        } else {
            // 이미지 다운로드 및 설정
            Glide.with(binding.itemImageUpdateView.context)
                .load(userItem.itemimage)
                .into(binding.itemImageUpdateView)
        }

        binding.viewModel!!.setOriginItemInfo(
            userItem.itemname.toString(),
            userItem.itemstore.toString(),
            userItem.itemname.toString(),
            userItem.itemimage.toString())
    }


    override fun updateImageBitmap(imageBitmap: Bitmap) {
        Log.d(TAG, "ItemUpdateFragment - updateImageBitmap() bitmap - $imageBitmap")
        binding.itemImageUpdateView.loadBitmap(imageBitmap)
        binding.viewModel!!.setItemImage("bitmap", BitmapConverter().bitmapToString(imageBitmap))
    }

    override fun updateImageURL(imageUri: Uri) {
        Log.d(TAG, "ItemUpdateFragment - updateImageURL() uri - $imageUri")
        binding.itemImageUpdateView.loadUrl(imageUri.toString())
        binding.viewModel!!.setItemImage("uri", ImageUtils.getRealPathFromURI(requireContext(), imageUri).toString())
    }

    override fun onItemUpdateSuccess(message: String?) {
        Log.d(TAG, "ItemUpdateFragment - onItemUpdateSuccess() called")
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        dismiss()
        (activity as? MainPage)?.restartItemListFragment()
    }

    override fun onItemUpdateError(message: String?) {
        Log.d(TAG, "ItemUpdateFragment - onItemUpdateError() called")
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.itemImageUpdate -> {
                Log.d(TAG, "ItemUpdateFragment - itemImageUpdate onClick() called")
                CameraGalleryDialogFragment().show(parentFragmentManager.beginTransaction(), "CameraGalleryDialogFragment")
            }
            R.id.plusUpdateBtn -> {
                Log.d(TAG, "ItemUpdateFragment - plusUpdateBtn onClick() called")
                val currentValue = binding.itemCountUpdateEdit.text.toString().toIntOrNull() ?: 0
                val afterValue = currentValue + 1
                binding.itemCountUpdateEdit.setText(afterValue.toString())
            }
            R.id.minusUpdateBtn -> {
                Log.d(TAG, "ItemUpdateFragment - minusUpdateBtn onClick() called")
                val currentValue = binding.itemCountUpdateEdit.text.toString().toIntOrNull() ?: 0
                val afterValue = currentValue - 1
                if (afterValue > 0)
                    binding.itemCountUpdateEdit.setText(afterValue.toString())
            }
            R.id.itemUpdateBtn -> {
                Log.d(TAG, "ItemUpdateFragment - itemUpdateBtn onClick() called")
                binding.viewModel!!.onItemUpdate()
            }
            R.id.itemUpdateBack -> {
                Log.d(TAG, "ItemUpdateFragment - itemUpdateBack onClick() called")
                dismiss()
            }
        }
    }
}