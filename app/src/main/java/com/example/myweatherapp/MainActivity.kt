package com.example.myweatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.myweatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.ActivityRetainedLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val navHostFragment: Fragment = requireNotNull(
        supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)
    ) { "NavHostFragment not found in ${this.javaClass.simpleName}" }

    val navFragmentId: Int = R.id.main_nav_host_fragment
    val navigationGraph: Int = R.navigation.my_navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
