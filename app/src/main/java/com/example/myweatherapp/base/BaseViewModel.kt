package com.example.myweatherapp.base

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.data.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _progressVisible = MutableStateFlow(false)
    val progressVisible = _progressVisible.asStateFlow()

    override fun onCleared() {
        scope.cancel()
        hideProgressBar()
        super.onCleared()
    }

    // region coroutines
    @WorkerThread
    protected suspend fun <T> io(block: suspend CoroutineScope.() -> T) =
        withContext(DispatchersProvider.io) {
            block()
        }

    @MainThread
    protected suspend fun <T> main(block: suspend CoroutineScope.() -> T) =
        withContext(DispatchersProvider.main) {
            block()
        }

    protected fun launch(
        onError: (Throwable) -> Unit = ::onError,
        block: suspend CoroutineScope.() -> Unit,
    ): Job = scope.launch(CoroutineExceptionHandler { _, exception -> onError(exception) }) {
        block()
    }

    protected fun <T> async(
        onError: (Throwable) -> Unit = ::onError,
        block: suspend CoroutineScope.() -> T,
    ): Deferred<T> =
        scope.async(CoroutineExceptionHandler { _, exception -> onError(exception) }) {
            block()
        }

    protected fun Job?.relaunch(block: suspend CoroutineScope.() -> Unit): Job {
        this?.cancel()
        return launch(block = block)
    }
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

    protected inline fun <G> Resource<G?>.withCatchAndShowError(
        onSuccess: (G) -> Unit
    ) {
        when (this) {
            is Resource.Success -> {
                data?.let { data ->
                    onSuccess.invoke(data)
                } ?: onError(Throwable("Error response parsing"))
            }
            else -> showMessage(throwable?.message.toString())
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