package com.example.todolist.ui.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.todolist.databinding.AddBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: AddBottomSheetBinding
    private var listener: PhotoPickerListener? = null

    interface PhotoPickerListener {
        fun onPhotoSelected(uri: Uri?)  // Retorna URI da galeria
        fun onPhotoCaptured(bitmap: Bitmap?)  // Retorna Bitmap da câmera
    }

    fun setPhotoPickerListener(listener: PhotoPickerListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Permissão negada!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGallery.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), getStoragePermission()) == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(requireContext(), "Permissão negada!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getStoragePermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_GALLERY)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    val selectedImageUri: Uri? = data?.data
                    listener?.onPhotoSelected(selectedImageUri)
                }
                REQUEST_CAMERA -> {
                    val photo: Bitmap? = data?.extras?.get("data") as? Bitmap
                    listener?.onPhotoCaptured(photo)
                }
            }
            dismiss()
        }
    }

    companion object {
        private const val REQUEST_GALLERY = 100
        private const val REQUEST_CAMERA = 101

        fun newInstance(): AddBottomSheet {
            return AddBottomSheet()
        }
    }
}
