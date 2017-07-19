package org.imozerov.babylonapp.util

import android.arch.lifecycle.LiveData

import org.imozerov.babylonapp.api.ApiResponse

import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response

class LiveDataCallAdapter<R>(private val responseType: Type) : CallAdapter<R, LiveData<ApiResponse<R>>> {
    override fun responseType() = responseType

    override fun adapt(call: Call<R>) = object : LiveData<ApiResponse<R>>() {
            internal var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(newCall: Call<R>, response: Response<R>) {
                            postValue(ApiResponse(response))
                        }

                        override fun onFailure(newCall: Call<R>, throwable: Throwable) {
                            postValue(ApiResponse<R>(throwable))
                        }
                    })
                }
            }
    }
}
