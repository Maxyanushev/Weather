package com.example.myweatherapp.fragments

import com.example.myweatherapp.R
import com.example.myweatherapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(override val viewState: HomeViewState): BaseViewModel<HomeViewState>() {
    fun openDataFragment() {
        navigateTo(R.id.dataFragment)
    }
}