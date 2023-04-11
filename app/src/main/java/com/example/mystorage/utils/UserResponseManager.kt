package com.example.mystorage.utils

import com.example.mystorage.mvvm.model.UserRegistrationResponse

class UserResponseManager private constructor() {
    companion object {
        private const val TAG = "UserResponseManager"
        private var instance: UserResponseManager? = null

        fun getInstance(): UserResponseManager {
            if (instance == null) {
                instance = UserResponseManager()
            }
            return instance!!
        }
    }

    interface OnResponseCompleteListener {
        fun onSuccess(message: String)
        fun onError(message: String)
    }

    fun userRegistrationResponse(response: UserRegistrationResponse, onResponseCompleteListener: OnResponseCompleteListener) {
        val message = response.message

        if (response.status == "true") {
            onResponseCompleteListener.onSuccess(message)
        }
        else {
            onResponseCompleteListener.onError(message)
        }
    }
}