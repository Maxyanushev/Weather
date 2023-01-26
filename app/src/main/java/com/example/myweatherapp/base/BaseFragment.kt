package com.example.myweatherapp.base

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : ViewDataBinding, U> : Fragment() {

    companion object {
        private const val DEFAULT_DEBOUNCE_MS = 300L
    }

    private var _binding: T? = null

    protected val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @LayoutRes
    open fun getLayoutResId(): Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
            ?: throw RuntimeException("DataBindingUtil.inflate return null")
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    open fun canNavigateUp(): Boolean {
        return false
    }

    fun View.clickWithDebounce(debounceTime: Long = DEFAULT_DEBOUNCE_MS, callback: (View) -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0
            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                else callback.invoke(v)
                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }
}