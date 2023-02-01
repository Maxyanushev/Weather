package com.example.myweatherapp.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.base.BaseViewModel
import com.example.myweatherapp.data.WeatherModel
import com.example.myweatherapp.data.repositories.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataFragmentViewModel @Inject constructor(override val viewState: DataViewState, private val repository: WeatherRepository) : BaseViewModel<DataViewState>() {

    val weather_data = MutableLiveData<WeatherModel?>()

    fun refreshData(cityName: String) {
        getDataFromAPI(cityName)
    }

    private fun getDataFromAPI(cityName: String) {
        launch {
            repository.getData(cityName).withCatchAndShowError {
                weather_data.postValue(it)
            }
        }
    }
}
