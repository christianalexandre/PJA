package com.example.todo.utils.dialog

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.todo.databinding.ItemDialogImageBinding

class ImageDialog: AppCompatDialogFragment() {

    private var binding: ItemDialogImageBinding? = null
    private var image: Bitmap? = null

    companion object {

        const val TAG = "ImageDialog"

        fun newInstance(image: Bitmap): ImageDialog = ImageDialog().apply {
            this.image = image
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = ItemDialogImageBinding.inflate(layoutInflater)

        binding?.taskImage?.setImageBitmap(image)

        return AlertDialog.Builder(requireContext())
            .setView(binding?.root)
            .setCustomTitle(null)
            .create()

    }

}