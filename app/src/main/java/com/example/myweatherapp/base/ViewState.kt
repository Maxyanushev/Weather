package com.example.myweatherapp.base

import androidx.databinding.BaseObservable
import javax.inject.Inject

abstract class ViewState : BaseObservable()

class EmptyViewState @Inject constructor() : ViewState()