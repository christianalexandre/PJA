package com.example.todo.utils.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.databinding.BottomSheetLayoutBinding
import com.example.todo.utils.listener.PhotoAccessListener

class PhotoAccessBottomSheetFragment(
    private val listener: PhotoAccessListener
): BaseBottomSheetFragment<BottomSheetLayoutBinding>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetLayoutBinding {
        return BottomSheetLayoutBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.buttonAccessCamera.setOnClickListener {
            listener.onAccessCamera()
            dismiss()
        }

        binding.buttonAccessGallery.setOnClickListener {
            listener.onAccessGallery()
            dismiss()
        }

    }

}