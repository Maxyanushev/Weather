package com.example.myweatherapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

abstract class BaseNavigationActivity<T : ViewDataBinding> : AppCompatActivity() {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    abstract fun createBinding(): T?

    abstract val navHostFragment: Fragment
    abstract val navFragmentId: Int
    abstract val navigationGraph: Int
    private var baseGraph: NavGraph? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = createBinding()
        setContentView(binding.root)
        setupNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        val isEndDest = findNavController(navFragmentId).navigateUp()
        if (!isEndDest) {
            finish()
        }
        return isEndDest
    }

    private fun setupNavigation(setGraph: Boolean = true): NavController {
        val navHostFragment = navHostFragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(navigationGraph)
        val navController = findNavController(navFragmentId)

        baseGraph = graph

        if (setGraph) {
            navController.setGraph(graph, intent?.extras)
        }

        getFragmentNavigator(navController)

        return navController
    }

    protected open fun getFragmentNavigator(navController: NavController) = Unit

    fun setupNavigationStartDestination() {
        baseGraph?.let {
            findNavController(navFragmentId).setGraph(it, intent?.extras)
        }
    }

    open fun setupNavigationToolbar() {

        val appBarConfiguration = AppBarConfiguration.Builder().build()
        val navController = findNavController(navFragmentId)

        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}