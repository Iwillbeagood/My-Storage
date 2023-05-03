package com.example.mystorage.mvvm.item.view.autoItemAdd

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentAutoItemAddBinding
import com.example.mystorage.mvvm.item.view.CameraGalleryDialogFragment
import com.example.mystorage.utils.*
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.ImageLoader.loadBitmap


class AutoItemAddFragment : DialogFragment(), AutoItemAddIView, View.OnClickListener {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
    }

    private lateinit var binding : FragmentAutoItemAddBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAutoItemAddBinding.inflate(inflater, container, false)
        binding.receiptImageView.setOnClickListener(this)
        binding.autoItemAddBack.setOnClickListener(this)
        binding.autoItemAddBtn.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.receiptImageView -> {
                Log.d(TAG, "AutoItemAddFragment - receiptImageView onClick() called")
                if (binding.receiptImageView.isEnabled) {
                    binding.receiptImageView.isEnabled = false
                    CameraGalleryDialogFragment().show(parentFragmentManager.beginTransaction(), "CameraGalleryDialogFragment")
                    binding.receiptImageView.isEnabled = true
                }
            }
        }
    }

    override fun addReceiptBitmap(receiptBitmap: Bitmap) {
        Log.d(TAG, "AutoItemAddFragment - addReceiptBitmap() bitmap - $receiptBitmap")
        val bitmap = ImageProcessingUtil.extractReceipt("bitmap", BitmapConverter().bitmapToString(receiptBitmap))
        binding.receiptImageView.loadBitmap(bitmap)
    }

    override fun addReceiptURL(receiptUri: Uri) {
        Log.d(TAG, "AutoItemAddFragment - addReceiptURL() uri - $receiptUri")
        val bitmap = ImageProcessingUtil.extractReceipt(
            "uri",
            ImageUtils.getRealPathFromURI(requireContext(), receiptUri).toString()
        )
        binding.receiptImageView.loadBitmap(bitmap)
    }

    override fun onItemAddSuccess(message: String?) {
        Log.d(TAG, "AutoItemAddFragment - onItemAddSuccess() called")
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        dismiss()
    }

    override fun onItemAddError(message: String?) {
        Log.d(TAG, "AutoItemAddFragment - onItemAddError() called")
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}