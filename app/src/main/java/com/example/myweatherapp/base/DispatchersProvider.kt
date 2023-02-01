package com.example.myweatherapp.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object DispatchersProvider {
    var main: CoroutineDispatcher = Dispatchers.Main.immediate
        private set

    var io: CoroutineDispatcher = Dispatchers.IO
        private set
}
