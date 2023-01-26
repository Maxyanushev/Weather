package com.example.myweatherapp.base

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign

class StatePersistingNavHostFragment: NavHostFragment() {

    override fun onCreateNavController(navController: NavController) {
        super.onCreateNavController(navController)
        navController.navigatorProvider += StatePersistingFragmentNavigator(requireContext(), childFragmentManager, id)
    }
}