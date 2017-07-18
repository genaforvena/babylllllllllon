package org.imozerov.babylonapp.api

import android.util.Log
import retrofit2.Response
import java.io.IOException


class ApiResponse<T> {
    val code: Int
    val body: T?
    val errorMessage: String?

    constructor(error: Throwable) {
        code = 500
        body = null
        errorMessage = error.message
    }

    constructor(response: Response<T>) {
        code = response.code()
        if (response.isSuccessful) {
            body = response.body()
            errorMessage = null
        } else {
            var message: String? = null
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string()
                } catch (e: IOException) {
                    Log.e(TAG, "error while parsing response", e)
                }

            }
            if (message == null || message.trim { it <= ' ' }.isEmpty()) {
                message = response.message()
            }
            errorMessage = message
            body = null
        }
    }

    fun isSuccessful() = code >= 200 && code < 300

    companion object {
        private val TAG = "ApiResponse"
    }
}