package com.example.mystorage.mvvm.item.view.itemAddView

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
import com.example.mystorage.MainPage
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentItemAddDialogBinding
import com.example.mystorage.fragment.CameraGalleryDialogFragment
import com.example.mystorage.mvvm.item.view.itemList.ItemListFragment
import com.example.mystorage.mvvm.item.viewmodel.ItemAddViewModel
import com.example.mystorage.mvvm.item.viewmodel.ItemAddViewModelFactory
import com.example.mystorage.retrofit.response.UserHomeInfoResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.App
import com.example.mystorage.utils.BitmapConverter
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.DialogUtils
import com.example.mystorage.utils.ImageLoader.loadBitmap
import com.example.mystorage.utils.ImageLoader.loadUrl
import com.example.mystorage.utils.ImageUtils.getRealPathFromURI
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemAddDialogFragment : DialogFragment(), ItemAddDialogIView, View.OnClickListener {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
    }

    private lateinit var binding: FragmentItemAddDialogBinding
    private val mainPage = MainPage()
    var items = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_add_dialog, container, false)
        val itemAddViewModelFactory = ItemAddViewModelFactory(this)
        binding.viewModel = ViewModelProvider(this, itemAddViewModelFactory)[ItemAddViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "ItemAddDialogFragment - onViewCreated() called")

        binding.itemImage.setOnClickListener(this)
        binding.plusBtn.setOnClickListener(this)
        binding.minusBtn.setOnClickListener(this)
        binding.itemAddBtn.setOnClickListener(this)
        binding.itemAddBack.setOnClickListener(this)

        // 장소 선택을 위한 스피너 설정
        val spinner = binding.itemPlaceSpinner
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
                        val loadedItems = userHomeInfoLoadResponse(response.body()!!)
                        items.addAll(loadedItems)
                        adapter.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        onItemAddError("응답 결과 파싱 중 오류가 발생했습니다")
                    }
                }
            }
            override fun onFailure(call: Call<UserHomeInfoResponse>, t: Throwable) {
                onItemAddError("통신 실패")
                call.cancel()
            }
        })

    }

    override fun userHomeInfoLoadResponse(response: UserHomeInfoResponse): MutableList<String> {
        val items = mutableListOf<String>()
        if (response.status == "true") {
            val jsonObjectRoom = JSONObject(response.roomNames)
            val roomNames = jsonObjectRoom.getJSONArray("room_names")
            for (i in 0 until roomNames.length()) {
                val roomName = roomNames.getString(i)
                items.add(roomName)
            }

            val jsonObjectBath = JSONObject(response.bathroomNames)
            val bathNames = jsonObjectBath.getJSONArray("bath_names")
            for (i in 0 until bathNames.length()) {
                val bathName = bathNames.getString(i)
                items.add(bathName)
            }

            if (response.livingRoomName != "없음") items.add("거실")
            if (response.kitchenName != "없음") items.add("주방")
            if (response.storageName != "없음") items.add("창고")
            if (response.etc_name != "없음") items.add(response.etc_name)
        }

        return items
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.itemImage -> {
                Log.d(TAG, "ItemAddDialogFragment - itemImage onClick() called")
                CameraGalleryDialogFragment().show(parentFragmentManager.beginTransaction(), "CameraGalleryDialogFragment")
            }
            R.id.plusBtn -> {
                Log.d(TAG, "ItemAddDialogFragment - plusBtn onClick() called")
                val currentValue = binding.itemCountEdit.text.toString().toIntOrNull() ?: 0
                val afterValue = currentValue + 1
                binding.itemCountEdit.setText(afterValue.toString())
            }
            R.id.minusBtn -> {
                Log.d(TAG, "ItemAddDialogFragment - minusBtn onClick() called")
                val currentValue = binding.itemCountEdit.text.toString().toIntOrNull() ?: 0
                val afterValue = currentValue - 1
                if (afterValue > 0)
                    binding.itemCountEdit.setText(afterValue.toString())
            }
            R.id.itemAddBtn -> {
                Log.d(TAG, "ItemAddDialogFragment - itemAddBtn onClick() called")
                binding.viewModel!!.onItemAdd()
            }
            R.id.itemAddBack -> {
                Log.d(TAG, "ItemAddDialogFragment - itemAddBack onClick() called")
                dismiss()
            }
        }
    }

    override fun updateImageBitmap(imageBitmap: Bitmap) {
        Log.d(TAG, "ItemAddDialogFragment - updateImageBitmap() bitmap - $imageBitmap")
        binding.itemImageView.loadBitmap(imageBitmap)
        binding.viewModel!!.setItemImage(BitmapConverter().bitmapToString(imageBitmap))
    }

    override fun updateImageURL(imageUri: Uri) {
        Log.d(TAG, "ItemAddDialogFragment - updateImageURL() uri - $imageUri")
        binding.itemImageView.loadUrl(imageUri.toString())
        binding.viewModel!!.setItemImage(getRealPathFromURI(requireContext(), imageUri).toString())
        Log.d(TAG, "ItemAddDialogFragment - updateImageURL() fileUri - ${getRealPathFromURI(requireContext(), imageUri).toString()}")

    }

    override fun onItemAddSuccess(message: String?) {
        Log.d(TAG, "ItemAddDialogFragment - onItemAddSuccess() called")
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        dismiss()
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host, ItemListFragment())
            .commit()
    }

    override fun onItemAddError(message: String?) {
        Log.d(TAG, "ItemAddDialogFragment - onItemAddError() called")
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemCountUpdate(message: String?) {
        Log.d(TAG, "ItemAddDialogFragment - onItemUpdate() called")

        DialogUtils.showDialog(
            requireActivity(),
            message.toString(),
            "확인",
            "취소",
            onPositiveClick = {
                binding.viewModel!!.getResponseOnItemAdd(false)
            },
            onNegativeClick = {
            }
        )
    }

}