package org.imozerov.babylonapp.testutil

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import org.imozerov.babylonapp.api.ApiResponse

import retrofit2.Response

fun <T> successCall(data: T): LiveData<ApiResponse<T>> {
    return createCall(Response.success(data))
}

private fun <T> createCall(response: Response<T>): LiveData<ApiResponse<T>> {
    val data = MutableLiveData<ApiResponse<T>>()
    data.value = ApiResponse(response)
    return data
}
