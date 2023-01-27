package com.example.myweatherapp.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.myweatherapp.R
import com.example.myweatherapp.base.DataBindingBaseFragment
import com.example.myweatherapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : DataBindingBaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun getLayoutResId() = R.layout.fragment_home
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            viewModel.openDataFragment()
        }
    }

    override fun onBindViewModel(viewModel: HomeViewModel) {
        super.onBindViewModel(viewModel)
        viewModel.apply {
            navigationEvent.observe(this@HomeFragment) {
                findNavController().navigate(it)
            }
        }
    }
}