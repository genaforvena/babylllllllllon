package org.imozerov.babylonapp.model

data class Result<T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?) = Result(Status.SUCCESS, data, null)

        fun <T> error(msg: String, data: T?) = Result(Status.ERROR, data, msg)

        fun <T> loading(data: T?) = Result(Status.LOADING, data, null)
    }

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}