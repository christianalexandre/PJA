package com.example.todo.utils.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.todo.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<T: ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    companion object {
        const val TAG = "BottomSheet"
    }

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): T

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    final override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    final override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}