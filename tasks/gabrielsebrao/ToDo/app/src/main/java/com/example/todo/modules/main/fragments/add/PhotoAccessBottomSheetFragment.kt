package com.example.todo.modules.main.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.todo.databinding.ItemBottomSheetLayoutBinding
import com.example.todo.utils.bottomsheet.BaseBottomSheetFragment
import com.example.todo.utils.listener.PhotoAccessListener

class PhotoAccessBottomSheetFragment(
    private val listener: PhotoAccessListener
): BaseBottomSheetFragment<ItemBottomSheetLayoutBinding>() {
    
    private var hasImage = false

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): ItemBottomSheetLayoutBinding {
        return ItemBottomSheetLayoutBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        
        if(hasImage)
            binding.buttonCancelImage.visibility = View.VISIBLE
        else 
            binding.buttonCancelImage.visibility = View.GONE

        binding.buttonAccessCamera.setOnClickListener {
            listener.onAccessCamera()
        }

        binding.buttonAccessGallery.setOnClickListener {
            listener.onAccessGallery()
        }

        binding.buttonCancelImage.setOnClickListener {

            it.postDelayed({
                listener.onDeleteImage()
            }, 150) // in order to show ripple effect

        }

    }

    fun show(manager: FragmentManager, tag: String?, hasImage: Boolean) {
        this.show(manager, tag)
        this.hasImage = hasImage
    }

}