package com.example.myweatherapp.fragments

import android.annotation.SuppressLint
import com.bumptech.glide.Glide
import com.example.myweatherapp.R
import com.example.myweatherapp.base.DataBindingBaseFragment
import com.example.myweatherapp.databinding.FragmentDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataFragment : DataBindingBaseFragment<FragmentDataBinding, DataFragmentViewModel>() {

    override fun getLayoutResId() = R.layout.fragment_data

    @SuppressLint("SetTextI18n")
    override fun onBindViewModel(viewModel: DataFragmentViewModel) {
        super.onBindViewModel(viewModel)

        viewModel.weather_data.observe(this@DataFragment) {
            with(binding){
                textCityCode.text = it?.sys?.country
                textCityName.text = it?.name
                textWindSpeed.text = it?.wind?.speed.toString()
                textHumidity.text = it?.clouds?.all.toString() + "%"
                textLat.text = it?.coord?.lat.toString()
                textLon.text = it?.coord?.lon.toString()
                textDegree.text = (it?.main?.temp?.minus(273)?.toInt()).toString() + "°C"

                Glide.with(requireActivity())
                    .load("https://openweathermap.org/img/wn/" + it?.weather?.get(0)?.icon + "@2x.png")
                    .into(imageWeatherPictures)
            }
        }

        binding.imageSearchCity.setOnClickListener {
            viewModel.refreshData(binding.editCityName.text.toString())
        }
    }
}