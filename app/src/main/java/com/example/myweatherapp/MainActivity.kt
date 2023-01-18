package com.example.myweatherapp

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewmodel: MainViewModel

    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    private val edit_city_name: EditText = findViewById(R.id.edit_city_name)

    private val linear_data: LinearLayout = findViewById(R.id.linear_data)

    private val text_city_code: TextView = findViewById(R.id.text_city_code)
    private val text_city_name: TextView = findViewById(R.id.text_city_name)
    private val text_degree: TextView = findViewById(R.id.text_degree)
    private val text_humidity: TextView = findViewById(R.id.text_humidity)
    private val text_wind_speed: TextView = findViewById(R.id.text_wind_speed)
    private val text_lat: TextView = findViewById(R.id.text_lat)
    private val text_lon: TextView = findViewById(R.id.text_lon)
    private val text_error: TextView = findViewById(R.id.text_error)

    private val image_weather_pictures: ImageView = findViewById(R.id.image_weather_pictures)
    private val image_search_city: ImageView = findViewById(R.id.image_search_city)

    private val progress_loading: ProgressBar = findViewById(R.id.progress_loading)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        val viewModelFactory = MyViewModelFactory(10)
        viewmodel = ViewModelProviders.of(baseContext, viewModelFactory).get(MainViewModel::class.java)
        viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val cName = GET.getString("cityName", "kharkiv")?.lowercase(Locale.ROOT)
        edit_city_name.setText(cName)
        viewmodel.refreshData(cName!!)

        getLiveData()

        image_search_city.setOnClickListener {
            val cityName = edit_city_name.text.toString()
            SET.putString("cityName", cityName)
            SET.apply()
            viewmodel.refreshData(cityName)
            getLiveData()
            Log.i(TAG, "onCreate: " + cityName)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getLiveData() {
        viewmodel.weather_data.observe(this) { data ->
            data?.let {
                linear_data.visibility = View.VISIBLE

                text_city_code.text = data.sys.country
                text_city_name.text = data.name

                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + data.weather.get(0).icon + "@2x.png")
                    .into(image_weather_pictures)

                text_degree.text = data.main.temp.toString() + "°C"
                text_humidity.text = data.main.humidity.toString()

                text_wind_speed.text = data.wind.speed.toString()
                text_lat.text = data.coord.lat.toString()
                text_lon.text = data.coord.lon.toString()
            }
        }

        viewmodel.weather_error.observe(this) { error ->
            error?.let {
                if (error) {
                    text_error.visibility = View.VISIBLE
                    progress_loading.visibility = View.GONE
//                    linear_data.visibility = View.GONE
                } else {
                    text_error.visibility = View.GONE
                }
            }
        }

        viewmodel.weather_loading.observe(this) { loading ->
            loading?.let {
                if (loading) {
                    progress_loading.visibility = View.VISIBLE
                    text_error.visibility = View.GONE
                    linear_data.visibility = View.GONE
                } else {
                    progress_loading.visibility = View.GONE
                }
            }
        }
    }
}
