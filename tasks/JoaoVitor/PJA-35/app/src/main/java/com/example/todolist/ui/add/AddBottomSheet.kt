package com.example.todolist.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todolist.databinding.AddBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddBottomSheet(private val listener: PhotoPickerListener) : BottomSheetDialogFragment() {

    private var _binding: AddBottomSheetBinding? = null
    private val binding get() = _binding!!

    interface PhotoPickerListener {
        fun onCameraSelected()
        fun onGallerySelected()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCamera.setOnClickListener {
            listener.onCameraSelected()
            dismiss()
        }

        binding.btnGallery.setOnClickListener {
            listener.onGallerySelected()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
