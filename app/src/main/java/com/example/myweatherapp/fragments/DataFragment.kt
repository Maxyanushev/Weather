package com.example.myweatherapp.fragments

import android.os.Bundle
import android.view.View
import com.example.myweatherapp.R
import com.example.myweatherapp.base.DataBindingBaseFragment
import com.example.myweatherapp.databinding.FragmentDataBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "DataFragment"
@AndroidEntryPoint
class DataFragment : DataBindingBaseFragment<FragmentDataBinding, DataFragmentViewModel>() {

    override fun getLayoutResId() = R.layout.fragment_data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onBindViewModel(viewModel: DataFragmentViewModel) {
        super.onBindViewModel(viewModel)

        binding.button2.setOnClickListener {
            viewModel.refreshData("lozova")
        }
    }
}