package com.example.myweatherapp.fragments

import com.example.myweatherapp.base.BaseViewModel
import com.example.myweatherapp.base.EmptyViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(override val viewState: EmptyViewState): BaseViewModel<EmptyViewState>() {
}