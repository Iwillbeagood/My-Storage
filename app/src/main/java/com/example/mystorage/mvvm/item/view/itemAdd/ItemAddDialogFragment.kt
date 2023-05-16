package com.example.mystorage.mvvm.item.view.itemAdd

import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentItemAddDialogBinding
import com.example.mystorage.mvvm.item.view.CameraGalleryDialogFragment
import com.example.mystorage.mvvm.item.view.itemList.ItemListFragment
import com.example.mystorage.mvvm.item.view.itemStructure.ItemStructureFragment
import com.example.mystorage.mvvm.item.viewmodel.itemAdd.ItemAddViewModel
import com.example.mystorage.mvvm.item.viewmodel.itemAdd.ItemAddViewModelFactory
import com.example.mystorage.retrofit.model.UserHomeInfoResponse
import com.example.mystorage.retrofit.retrofitManager.LoadUserHomeInfoManager
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.*
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.ImageLoader.loadBitmap
import com.example.mystorage.utils.ImageLoader.loadUrl
import com.example.mystorage.utils.ImageUtils.getRealPathFromURI
import org.json.JSONException
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

        FocusChangeListener.setEditTextFocusChangeListener(binding.itemNameEdit, binding.itemName)
        FocusChangeListener.setEditTextFocusChangeListener(binding.iteStoreEdit, binding.itemStore)

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

        // 스피너 아이템을 서버로부터 불러와서 설정
        val loadInfoManager = LoadUserHomeInfoManager.getInstance(requireContext())

        loadInfoManager.itemDelete(
            object : LoadUserHomeInfoManager.OnLoadInfoCompleteListener {
                override fun onSuccess(loadedItems: MutableList<String>) {
                    items.addAll(loadedItems)
                    adapter.notifyDataSetChanged()
                }

                override fun onError(message: String) {
                    onItemAddError(message)
                }

            }
        )
    }

    override fun addImageBitmap(imageBitmap: Bitmap) {
        Log.d(TAG, "ItemAddDialogFragment - addImageBitmap() bitmap - $imageBitmap")
        binding.itemImageView.loadBitmap(imageBitmap)
        binding.viewModel!!.setItemImage("bitmap", BitmapConverter().bitmapToString(imageBitmap))
    }

    override fun addImageURL(imageUri: Uri) {
        Log.d(TAG, "ItemAddDialogFragment - addImageURL() uri - $imageUri")
        binding.itemImageView.loadUrl(imageUri.toString())
        binding.viewModel!!.setItemImage("uri", getRealPathFromURI(requireContext(), imageUri).toString())

    }

    override fun onItemAddSuccess(message: String?) {
        Log.d(TAG, "ItemAddDialogFragment - onItemAddSuccess() called")
        CustomToast.createToast(requireActivity(), message.toString()).show()
        dismiss()

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val itemListFragment = parentFragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":1") as ItemListFragment?
        itemListFragment?.onResume() // itemListFragment 인스턴스를 가져와서 onResume 메소드 호출
        val itemStrFragment = parentFragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":0") as ItemStructureFragment?
        itemStrFragment?.onResume()
    }

    override fun onItemAddError(message: String?) {
        Log.d(TAG, "ItemAddDialogFragment - onItemAddError() called")
        CustomToast.createToast(requireActivity(), message.toString()).show()
    }

    override fun onItemCountUpdate(itemid: Int, message: String?) {
        Log.d(TAG, "ItemAddDialogFragment - onItemUpdate() called")

        DialogUtils.showDialog(
            requireActivity(),
            message.toString(),
            "확인",
            "취소",
            onPositiveClick = {
                binding.viewModel!!.onItemAdd("countUpdate", itemid)
            },
            onNegativeClick = {
            }
        )
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.itemImage -> {
                Log.d(TAG, "ItemAddDialogFragment - itemImage onClick() called")
                if (binding.itemImage.isEnabled) {
                    binding.itemImage.isEnabled = false
                    CameraGalleryDialogFragment().show(parentFragmentManager.beginTransaction(), "CameraGalleryDialogFragment")
                    binding.itemImage.isEnabled = true
                }
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
                if (binding.itemAddBtn.isEnabled) {
                    binding.itemAddBtn.isEnabled = false
                    binding.viewModel!!.onItemAdd("add", 0)
                    binding.itemAddBtn.isEnabled = true
                }
            }
            R.id.itemAddBack -> {
                Log.d(TAG, "ItemAddDialogFragment - itemAddBack onClick() called")
                if (binding.itemAddBack.isEnabled) {
                    binding.itemAddBack.isEnabled = false
                    dismiss()
                    binding.itemAddBack.isEnabled = true
                }
            }
        }
    }
}