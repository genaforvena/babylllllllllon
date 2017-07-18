package org.imozerov.babylonapp.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import org.imozerov.babylonapp.AppExecutors
import org.imozerov.babylonapp.api.ApiResponse
import org.imozerov.babylonapp.model.Result

abstract class NetworkBoundResource<ResultType, RequestType> @MainThread
internal constructor(private val appExecutors: AppExecutors) {
    private val result = MediatorLiveData<Result<ResultType>>()

    init {
        result.setValue(Result.loading(null))
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData -> result.setValue(Result.success(newData)) }
            }
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        result.addSource(dbSource) { newData -> result.setValue(Result.loading(newData)) }

        result.addSource<ApiResponse<RequestType>>(apiResponse) { response ->
            result.removeSource<ApiResponse<RequestType>>(apiResponse)
            result.removeSource(dbSource)

            if (response?.isSuccessful() == true) {
                appExecutors.diskIO.execute({
                    saveCallResult(processResponse(response))
                    appExecutors.mainThread.execute({
                        result.addSource(loadFromDb()
                        ) { newData -> result.setValue(Result.success(newData)) }
                    })
                })
            } else {
                onFetchFailed()
                result.addSource(dbSource) {
                    newData -> result.setValue(Result.error(response!!.errorMessage!!, newData))
                }
            }
        }
    }

    protected fun onFetchFailed() {}

    fun asLiveData() = result

    @WorkerThread
    protected fun processResponse(response: ApiResponse<RequestType>): RequestType {
        return response.body!!
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}