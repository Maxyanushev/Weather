package com.example.myweatherapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.base.BaseViewModel
import com.example.myweatherapp.base.ViewState
import com.example.myweatherapp.fragments.DataViewState
import com.example.myweatherapp.model.WeatherModel
import com.example.myweatherapp.services.WeatherAPI
import com.example.myweatherapp.services.WeatherAPIService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class DataFragmentViewModel @Inject constructor(override val viewState: DataViewState) : BaseViewModel<DataViewState>() {

    private val weatherApi: WeatherAPI ?= null
    private val weatherApiService = weatherApi?.let { WeatherAPIService(it) }
    private val disposable = CompositeDisposable()

    val weather_data = MutableLiveData<WeatherModel>()
    val weather_error = MutableLiveData<Boolean>()
    val weather_loading = MutableLiveData<Boolean>()

    fun refreshData(cityName: String) {
        getDataFromAPI(cityName)
    }

    private fun getDataFromAPI(cityName: String) {

        weather_loading.value = true
        weatherApiService?.getDataService(cityName)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())?.let {
                disposable.add(
                it
                    .subscribeWith(object : DisposableSingleObserver<WeatherModel>() {

                        override fun onSuccess(t: WeatherModel) {
                            weather_data.value = t
                            weather_error.value = false
                            weather_loading.value = false
                            Log.d(TAG, "onSuccess: Success")
                        }

                        override fun onError(e: Throwable) {
                            weather_error.value = true
                            weather_loading.value = false
                            Log.e(TAG, "onError: $e")
                        }
                    })
            )
            }
    }
}