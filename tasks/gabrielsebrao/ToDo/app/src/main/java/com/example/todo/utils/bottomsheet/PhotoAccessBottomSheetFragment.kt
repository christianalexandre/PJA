package com.example.todo.utils.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.databinding.ItemBottomSheetLayoutBinding
import com.example.todo.utils.listener.PhotoAccessListener

class PhotoAccessBottomSheetFragment(
    private val listener: PhotoAccessListener
): BaseBottomSheetFragment<ItemBottomSheetLayoutBinding>() {

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): ItemBottomSheetLayoutBinding {
        return ItemBottomSheetLayoutBinding.inflate(inflater, container, false)
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