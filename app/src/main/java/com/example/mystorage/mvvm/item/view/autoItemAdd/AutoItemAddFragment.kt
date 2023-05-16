package com.example.mystorage.mvvm.item.view.autoItemAdd

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentAutoItemAddBinding
import com.example.mystorage.mvvm.item.model.ocr.OcrResponse
import com.example.mystorage.mvvm.item.view.CameraGalleryDialogFragment
import com.example.mystorage.retrofit.retrofitManager.NaverOcrManager
import com.example.mystorage.utils.*
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.ImageLoader.loadBitmap


class AutoItemAddFragment : DialogFragment(), AutoItemAddIView, View.OnClickListener {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(R.style.DialogLeftAnimation)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(R.color.white)
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
        binding.receiptImageView.loadBitmap(receiptBitmap)
        val bitmap = ImageProcessingUtil.receiptImageProcessing("bitmap", BitmapConverter().bitmapToString(receiptBitmap))
        if (bitmap != null) {
            ocrImage(bitmap)
        }
    }

    override fun addReceiptURL(receiptUri: Uri) {
        Log.d(TAG, "AutoItemAddFragment - addReceiptURL() uri - $receiptUri")
//        binding.receiptImageView.loadUrl(receiptUri.toString())
        val bitmap = ImageProcessingUtil.receiptImageProcessing(
            "uri",
            ImageUtils.getRealPathFromURI(requireContext(), receiptUri).toString()
        )
        if (bitmap != null) {
            ocrImage(bitmap)
            binding.receiptImageView.loadBitmap(bitmap)

        }

    }

    override fun onItemAddSuccess(message: String?) {
        Log.d(TAG, "AutoItemAddFragment - onItemAddSuccess() called")
        CustomToast.createToast(requireActivity(), message.toString()).show()
        dismiss()
    }

    override fun onItemAddError(message: String?) {
        Log.d(TAG, "AutoItemAddFragment - onItemAddError() called")
        CustomToast.createToast(requireActivity(), message.toString()).show()
    }

    fun ocrImage(bitmap: Bitmap) {
        Log.d(TAG, "AutoItemAddFragment - ocrImage() called")
        val naverOcrManager = NaverOcrManager.getInstance(requireContext())

        naverOcrManager.ocrImage(
            bitmap,
            object : NaverOcrManager.OnOcrCompleteListener {
                override fun onSuccess(ocrResult: MutableList<String>) {
                    Log.d(TAG, "AutoItemAddFragment - onSuccess()\n$ocrResult")
                }

                override fun onError(message: String) {
                    onItemAddError(message)
                }
            }
        )
    }


    fun responseOnOcrImage(ocrResponse: OcrResponse) {
        val result = ocrResponse.images.first()

        if (result.inferResult != "SUCCESS" || result.message != "SUCCESS") {
            Log.d(TAG, "AutoItemAddFragment - responseOnOcrImage() called")
            return
        }
        Log.d(TAG, "AutoItemAddFragment - responseOnOcrImage() called")
    }

}