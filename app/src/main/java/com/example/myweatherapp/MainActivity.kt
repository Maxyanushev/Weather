package com.example.myweatherapp

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.myweatherapp.databinding.ActivityMainBinding
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewmodel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java)
//        viewmodel = ViewModelProvider(this)[MainViewModel::class.java]

        val cName = GET.getString("cityName", "lozova")?.lowercase(Locale.ROOT)
        binding.editCityName.setText(cName)
        viewmodel.refreshData(cName!!)

        getLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.linearData.visibility = View.GONE
            binding.textError.visibility = View.GONE
            binding.progressLoading.visibility = View.GONE

            val cityName = GET.getString("cityName", cName)?.lowercase(Locale.ROOT)
            binding.editCityName.setText(cityName)
            viewmodel.refreshData(cityName!!)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.imageSearchCity.setOnClickListener {
            val cityName = binding.editCityName.text.toString()
            SET.putString("cityName", cityName)
            SET.apply()
            viewmodel.refreshData(cityName)
            getLiveData()
            Log.i(TAG, "onCreate: $cityName")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getLiveData() {
        viewmodel.weather_data.observe(this) { data ->
            data?.let {
                binding.linearData.visibility = View.VISIBLE

                binding.textCityCode.text = data.sys.country
                binding.textCityName.text = data.name

                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + data.weather[0].icon + "@2x.png")
                    .into(binding.imageWeatherPictures)

                binding.textDegree.text = data.main.temp.toString() + "°C"
                binding.textHumidity.text = data.main.humidity.toString()

                binding.textWindSpeed.text = data.wind.speed.toString()
                binding.textLat.text = data.coord.lat.toString()
                binding.textLon.text = data.coord.lon.toString()
            }
        }

        viewmodel.weather_error.observe(this) { error ->
            error?.let {
                if (error) {
                    binding.textError.visibility = View.VISIBLE
                    binding.progressLoading.visibility = View.GONE
                    binding.linearData.visibility = View.GONE
                } else {
                    binding.textError.visibility = View.GONE
                }
            }
        }

        viewmodel.weather_loading.observe(this) { loading ->
            loading?.let {
                if (loading) {
                    binding.progressLoading.visibility = View.VISIBLE
                    binding.textError.visibility = View.GONE
                    binding.linearData.visibility = View.GONE
                } else {
                    binding.progressLoading.visibility = View.GONE
                }
            }
        }
    }
}
