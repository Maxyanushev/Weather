package com.example.myweatherapp

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class MyViewModelFactory(val arg: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Int::class.java).newInstance(arg)
    }
}