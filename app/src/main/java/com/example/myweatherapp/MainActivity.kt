package com.example.myweatherapp

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewmodel: MainViewModel

    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    private lateinit var swipe_refresh_layout: SwipeRefreshLayout

    private lateinit var edit_city_name: EditText

    private lateinit var linear_data: LinearLayout

    private lateinit var text_city_code: TextView
    private lateinit var text_city_name: TextView
    private lateinit var text_degree: TextView
    private lateinit var text_humidity: TextView
    private lateinit var text_wind_speed: TextView
    private lateinit var text_lat: TextView
    private lateinit var text_lon: TextView
    private lateinit var text_error: TextView

    private lateinit var image_weather_pictures: ImageView
    private lateinit var image_search_city: ImageView

    private lateinit var progress_loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout)

        edit_city_name = findViewById(R.id.edit_city_name)

        linear_data = findViewById(R.id.linear_data)

        text_city_code = findViewById(R.id.text_city_code)
        text_city_name = findViewById(R.id.text_city_name)
        text_degree = findViewById(R.id.text_degree)
        text_humidity = findViewById(R.id.text_humidity)
        text_wind_speed = findViewById(R.id.text_wind_speed)
        text_lat = findViewById(R.id.text_lat)
        text_lon = findViewById(R.id.text_lon)
        text_error = findViewById(R.id.text_error)

        image_weather_pictures = findViewById(R.id.image_weather_pictures)
        image_search_city = findViewById(R.id.image_search_city)

        progress_loading = findViewById(R.id.progress_loading)

        viewmodel = ViewModelProvider(this)[MainViewModel::class.java]

        val cName = GET.getString("cityName", "london")?.lowercase(Locale.ROOT)
        edit_city_name.setText(cName)
        viewmodel.refreshData(cName!!)

        getLiveData()

        swipe_refresh_layout.setOnRefreshListener {
            linear_data.visibility = View.GONE
            text_error.visibility = View.GONE
            progress_loading.visibility = View.GONE

            val cityName = GET.getString("cityName", cName)?.lowercase(Locale.ROOT)
            edit_city_name.setText(cityName)
            viewmodel.refreshData(cityName!!)
            swipe_refresh_layout.isRefreshing = false
        }

        image_search_city.setOnClickListener {
            val cityName = edit_city_name.text.toString()
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
                linear_data.visibility = View.VISIBLE

                text_city_code.text = data.sys.country
                text_city_name.text = data.name

                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + data.weather[0].icon + "@2x.png")
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
                    linear_data.visibility = View.GONE
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
