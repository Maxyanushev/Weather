package com.example.myweatherapp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.myweatherapp.R
import com.example.myweatherapp.base.DataBindingBaseFragment
import com.example.myweatherapp.databinding.FragmentDataBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DataFragment : DataBindingBaseFragment<FragmentDataBinding, DataFragmentViewModel>() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    val addresses: List<Address>? =
                        it?.latitude?.let { it1 -> it.longitude.let { it2 ->
                            geocoder.getFromLocation(it1,
                                it2, 1)
                        } }
                    binding.editCityName.setText(addresses?.get(0)?.locality)
                    viewModel.refreshData(binding.editCityName.text.toString())
                }
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
            // No location access granted.
        }
        }
    }

    private var myLat: Double ?= 0.0
    private var myLon: Double ?= 0.0
    private lateinit var geocoder: Geocoder

    override fun getLayoutResId() = R.layout.fragment_data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        geocoder = Geocoder(requireActivity(), Locale.ENGLISH)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

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

                myLat = it?.coord?.lat
                myLon = it?.coord?.lon

                Glide.with(requireActivity())
                    .load("https://openweathermap.org/img/wn/" + it?.weather?.get(0)?.icon + "@2x.png")
                    .into(imageWeatherPictures)
            }
        }

        binding.imageSearchCity.setOnClickListener {
            viewModel.refreshData(binding.editCityName.text.toString())
        }

        binding.btnMyPosition.setOnClickListener {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }
}