package com.example.myweatherapp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.data.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

abstract class BaseViewModel<T : ViewState> : ViewModel() {

    private val _navigationEvent = SingleLiveEvent<Int>()
    val navigationEvent: LiveData<Int> = _navigationEvent
    private val _messageDataEvent = SingleLiveEvent<String>()
    val messageDataEvent: LiveData<String> = _messageDataEvent
    private val _progressLD = SingleLiveEvent<Boolean>()
    val progressLD: LiveData<Boolean> = _progressLD

    abstract val viewState: T

    private val supervisorJob = SupervisorJob()
    protected val scope = CoroutineScope(Dispatchers.IO + supervisorJob)

    protected fun navigateTo(actionId: Int) {
        _navigationEvent.postValue(actionId)
    }

    protected fun showMessage(message: String) {
        _messageDataEvent.postValue(message)
    }

    protected open fun showProgressBar() {
        _progressLD.postValue(true)
    }

    protected open fun hideProgressBar() {
        _progressLD.postValue(false)
    }

    protected suspend fun withProgressBar(block: suspend () -> Unit) {
        showProgressBar()
        block()
        hideProgressBar()
    }

    protected inline fun <G> Resource<G?>.withCatchAndShowDefaultError(
        noinline onRequestFailed: ((Throwable?) -> Unit)? = null,
        noinline onPressErrorAction: (() -> Unit)? = null,
        onSuccess: (G) -> Unit,
    ) {
        when (this) {
            is Resource.Success -> {
                data?.let { data ->
                    onSuccess.invoke(data)
                } ?: onError(Throwable("Error response parsing"))
            }
            is Resource.Error -> showMessage(throwable?.localizedMessage.toString())
            is Resource.Loading -> TODO()
            is Resource.Offline -> TODO()
        }
    }

    protected open fun onError(throwable: Throwable?) {
        when (throwable) {
            else -> {
                Timber.e(throwable)
                throwable?.let { showMessage(it.message.toString()) }
            }
        }
    }
}