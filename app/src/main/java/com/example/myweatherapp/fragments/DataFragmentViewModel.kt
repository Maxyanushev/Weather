package com.example.myweatherapp.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.base.BaseViewModel
import com.example.myweatherapp.model.WeatherModel
import com.example.myweatherapp.data.WeatherAPI
import com.example.myweatherapp.data.repositories.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataFragmentViewModel @Inject constructor(override val viewState: DataViewState, private val repository: WeatherRepository) : BaseViewModel<DataViewState>() {

    private val weatherApi: WeatherAPI?= null
//    private val weatherApiService = weatherApi?.let { WeatherAPIService(it) }
    private val disposable = CompositeDisposable()

    val weather_data = MutableLiveData<WeatherModel?>()
    val weather_error = MutableLiveData<Boolean>()
    val weather_loading = MutableLiveData<Boolean>()

    fun refreshData(cityName: String) {
        getDataFromAPI(cityName)
    }

    private fun getDataFromAPI(cityName: String) {
        viewModelScope.launch {
            val data = repository.getData(cityName).body()
            if (data != null) {
                weather_data.postValue(data)
            }
        }
//        weather_loading.value = true
//        weatherApiService?.getDataService(cityName)
//            ?.subscribeOn(Schedulers.io())
//            ?.observeOn(AndroidSchedulers.mainThread())?.let {
//                disposable.add(
//                    it
//                        .subscribeWith(object : DisposableSingleObserver<WeatherModel>() {
//
//                            override fun onSuccess(t: WeatherModel) {
//                                weather_data.value = t
//                                weather_error.value = false
//                                weather_loading.value = false
//                                Log.d("GET DATA", "onSuccess: Success")
//                            }
//
//                            override fun onError(e: Throwable) {
//                                weather_error.value = true
//                                weather_loading.value = false
//                                Log.e("GET DATA", "onError: $e")
//                            }
//                        })
//                )
//            }
    }
}