package com.example.mystorage.mvvm.user.view.signOut

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mystorage.MainActivity
import com.example.mystorage.R
import com.example.mystorage.databinding.FragmentSignOutDialogBinding
import com.example.mystorage.retrofit.model.ApiResponse
import com.example.mystorage.retrofit.retrofitManager.RetrofitManager
import com.example.mystorage.utils.ActivityUtil
import com.example.mystorage.utils.App
import com.example.mystorage.utils.Constants.TAG
import com.example.mystorage.utils.CustomToast
import com.example.mystorage.utils.FocusChangeListener
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignOutDialogFragment : DialogFragment(), SignOutIView, View.OnClickListener {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(R.style.DialogLeftAnimation)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(R.color.white)
    }

    private lateinit var binding : FragmentSignOutDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "SignOutDialogFragment - onCreateView() called")
        binding = FragmentSignOutDialogBinding.inflate(inflater, container, false)
        binding.passwordEditInSignOut.setOnClickListener(this)
        binding.signOutBack.setOnClickListener(this)

        FocusChangeListener.setEditTextFocusChangeListener(binding.passwordEditInSignOut, binding.passwordInSignOut)
        return binding.root
    }



    override fun onSignOutSuccess(message: String?) {
        CustomToast.createToast(requireActivity(), message.toString()).show()
        dismiss()
    }

    override fun onSignOutError(message: String?) {
        CustomToast.createToast(requireActivity(), message.toString()).show()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.passwordEditInSignOut -> {
                if (binding.passwordEditInSignOut.isEnabled) {
                    binding.passwordEditInSignOut.isEnabled = false
                    Log.d(TAG, "SignOutDialogFragment - passwordEditInSignOut onClick() called")
                    onSignOut()
                    binding.passwordEditInSignOut.isEnabled = true
                }
            }
            R.id.signOutBack -> {
                if (binding.signOutBack.isEnabled) {
                    binding.signOutBack.isEnabled = false
                    Log.d(TAG, "SignOutDialogFragment - signOutBack onClick() called")
                    dismiss()
                    binding.signOutBack.isEnabled = true
                }
            }
        }

    }

    override fun onSignOut() {
        val password = binding.passwordInSignOut.toString()
        if (password.isNotEmpty()) {
            onSignOutError("비밀번호를 입력해 주세요")
            return
        }

        val api = RetrofitManager.getSignOutApiService()

        val call = api.signOutUser(
            App.prefs.getString("userid", ""),
            password
        )

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body() != null) {
                    try {
                        userSignOutResponse(response.body()!!)
                    } catch (e: JSONException) {
                        onSignOutError("응답 결과 파싱 중 오류가 발생했습니다.")
                    }
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                onSignOutError("통신 실패")
                call.cancel()
            }
        })
    }

    override fun userSignOutResponse(response: ApiResponse) {
        val message = response.message
        when (response.status) {
            "true" -> {
                // 회원 탈퇴를 완료했음으로 초기 페이지로 이동
                ActivityUtil.goToNextActivity(requireActivity(), MainActivity())
            }
            "false" -> {
                // 회원 탈퇴를 하지 못했음으로 메시지 출력
                onSignOutError(message)
            }
        }
    }

}