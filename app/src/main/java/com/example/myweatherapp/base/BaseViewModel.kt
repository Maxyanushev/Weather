package com.example.myweatherapp.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseViewModel<T : ViewState> : ViewModel() {

    abstract val viewState: T

    private val supervisorJob = SupervisorJob()
    protected val scope = CoroutineScope(Dispatchers.IO + supervisorJob)
}