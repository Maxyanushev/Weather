package com.example.myweatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myweatherapp.base.BaseNavigationActivity
import com.example.myweatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseNavigationActivity<ActivityMainBinding>() {
    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)

    override val navHostFragment: Fragment
        get() = requireNotNull(
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)
        ) { "NavHostFragment not found in ${this.javaClass.simpleName}" }
    override val navFragmentId: Int
        get() = R.id.main_nav_host_fragment
    override val navigationGraph: Int
        get() = R.navigation.my_navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}