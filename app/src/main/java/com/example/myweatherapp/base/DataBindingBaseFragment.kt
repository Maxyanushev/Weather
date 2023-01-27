package com.example.myweatherapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

abstract class DataBindingBaseFragment<T : ViewDataBinding, VM : BaseViewModel<*>> :
    BaseFragment<T, Any?>(), LifecycleObserver {

    protected val viewModel: VM by createViewModel(
        ((this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>).kotlin
    )

    @StringRes
    protected open val screenTitle: Int? = null

    protected open val handleBackNav: (() -> Unit?)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        onBindViewModel(viewModel)
        return binding.root
    }

    @CallSuper
    protected open fun onBindViewModel(viewModel: VM) {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun createViewModel(modelType: KClass<VM>): Lazy<VM> {
        if (!modelType.isSubclassOf(ViewModel::class)) {
            throw IllegalStateException("ViewModel should be a subclass of ViewModel::class")
        }

        return createViewModelLazy(modelType, { viewModelStore })
    }

    protected open fun showSnackBar(message: String?) {
        view?.let {
            if (message != null) {
                Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    protected open fun handleError(error: Throwable?) {
        error?.message?.let { showSnackBar(it) }
    }

    protected fun addOnBackPressedCallback(onBackPressed: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback(owner = viewLifecycleOwner) { onBackPressed() }
    }
}