package com.example.myweatherapp.data

sealed class Resource<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
    val showProgress: Boolean? = null
) {

    fun showLoading(): Boolean {
        return (this is Loading && data == null)
    }

    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null, showProgress: Boolean? = null) : Resource<T>(data, showProgress = showProgress)
    class Error<T>(data: T? = null, throwable: Throwable) : Resource<T>(data, throwable)
    class Offline<T>(data: T? = null) : Resource<T>(data)
}