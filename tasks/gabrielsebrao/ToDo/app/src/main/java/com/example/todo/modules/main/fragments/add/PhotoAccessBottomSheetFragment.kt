package com.example.todo.modules.main.fragments.add

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.todo.R
import com.example.todo.databinding.ItemBottomSheetLayoutBinding
import com.example.todo.utils.bottomsheet.BaseBottomSheetFragment
import com.example.todo.utils.listener.PhotoAccessListener

class PhotoAccessBottomSheetFragment: BaseBottomSheetFragment<ItemBottomSheetLayoutBinding>() {
    
    private var hasImage = false
    private var listener: PhotoAccessListener? = null
    private var requestCameraPermissionLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(isGranted)
            listener?.onAccessCamera()
    }
    private val requestGalleryPermissionLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(isGranted)
            listener?.onAccessGallery()
    }

    companion object {

        private const val ARG_HAS_IMAGE = "has_image"

        fun newInstance(listener: PhotoAccessListener, hasImage: Boolean)
        = PhotoAccessBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_HAS_IMAGE, hasImage)
                }
                this.listener = listener
            }

    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): ItemBottomSheetLayoutBinding {
        return ItemBottomSheetLayoutBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        hasImage = arguments?.getBoolean(ARG_HAS_IMAGE) ?: false
        
        if(hasImage)
            binding.buttonCancelImage.visibility = View.VISIBLE
        else 
            binding.buttonCancelImage.visibility = View.GONE

        binding.buttonAccessCamera.setOnClickListener {

            if(isCameraPermissionGranted())
                listener?.onAccessCamera()
            else if(!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                Toast.makeText(requireContext(), getString(R.string.allow_camera), Toast.LENGTH_SHORT).show()
            else
                requestCameraPermission()

        }

        binding.buttonAccessGallery.setOnClickListener {

            if(isGalleryPermissionGranted())
                listener?.onAccessGallery()
            else if(!shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES))
                Toast.makeText(requireContext(), getString(R.string.allow_gallery), Toast.LENGTH_SHORT).show()
            else
                requestGalleryPermission()

        }

        binding.buttonCancelImage.setOnClickListener {

            it.postDelayed({
                listener?.onDeleteImage()
            }, 150) // in order to show ripple effect

        }

    }

    private fun isCameraPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun isGalleryPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestCameraPermission() =
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)

    private fun requestGalleryPermission() =
        requestGalleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

}