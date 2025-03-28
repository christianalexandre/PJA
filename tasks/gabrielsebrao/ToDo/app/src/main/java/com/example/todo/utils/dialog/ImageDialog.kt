package com.example.todo.utils.dialog

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.todo.databinding.ItemDialogImageBinding
import com.example.todo.utils.extensions.toBitmap

class ImageDialog: BaseDialog() {

    private var binding: ItemDialogImageBinding? = null
    private var byteArrayImage: ByteArray? = null
    private val maxWidth: Int
        get() =
            activity?.windowManager?.currentWindowMetrics?.bounds?.width()?.minus(100) ?: 500
    private val maxHeight: Int
        get() =
            activity?.windowManager?.currentWindowMetrics?.bounds?.height()?.minus(100) ?: 700

    companion object {

        fun newInstance(byteArrayBitmap: ByteArray?): ImageDialog = ImageDialog().apply {
            this.byteArrayImage = byteArrayBitmap
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = ItemDialogImageBinding.inflate(layoutInflater)

        binding?.taskImage?.setImageBitmap(resizeBitmap(byteArrayImage?.toBitmap()))

        return AlertDialog.Builder(requireContext())
            .setView(binding?.root)
            .setCustomTitle(null)
            .create()

    }

    private fun resizeBitmap(
        bitmap: Bitmap?,
        maxWidth: Int = this.maxWidth,
        maxHeight: Int = this.maxHeight
    ): Bitmap? {

        if (bitmap == null)
            return null

        val width = bitmap.width
        val height = bitmap.height
        val scaleFactor = minOf(maxWidth.toFloat() / width, maxHeight.toFloat() / height)

        return Bitmap.createScaledBitmap(
            bitmap,
            (width * scaleFactor).toInt(),
            (height * scaleFactor).toInt(),
            true
        )
    }

}