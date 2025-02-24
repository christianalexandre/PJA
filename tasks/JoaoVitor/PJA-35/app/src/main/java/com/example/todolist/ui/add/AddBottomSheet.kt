package com.example.todolist.ui.add

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todolist.databinding.AddBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddBottomSheet(private val listener: PhotoPickerListener) : BottomSheetDialogFragment() {


    private lateinit var binding: AddBottomSheetBinding

    interface PhotoPickerListener {
        fun onPhotoSelected(uri: Uri?)  // Retorna URI da galeria
        fun onPhotoCaptured(bitmap: Bitmap?)  // Retorna Bitmap da câmera
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

        // Abrir a câmera
        binding.btnCamera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_CAMERA)
        }

        // Abrir a galeria
        binding.btnGallery.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, REQUEST_GALLERY)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    val selectedImageUri: Uri? = data?.data
                    listener.onPhotoSelected(selectedImageUri)
                }
                REQUEST_CAMERA -> {
                    val photo: Bitmap? = data?.extras?.get("data") as? Bitmap
                    listener.onPhotoCaptured(photo)
                }
            }
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }

    companion object {
        private const val REQUEST_GALLERY = 100
        private const val REQUEST_CAMERA = 101
    }
}
