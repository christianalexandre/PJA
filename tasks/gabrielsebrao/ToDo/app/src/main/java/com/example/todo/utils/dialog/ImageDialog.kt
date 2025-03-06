package com.example.todo.utils.dialog

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.todo.databinding.ItemDialogImageBinding

class ImageDialog: BaseDialog() {

    private var binding: ItemDialogImageBinding? = null
    private var image: Bitmap? = null

    companion object {

        fun newInstance(filePath: String?): ImageDialog = ImageDialog().apply {
            this.image = loadImageFromPath(filePath)
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = ItemDialogImageBinding.inflate(layoutInflater)

        binding?.taskImage?.setImageBitmap(resizeBitmap(image))

        return AlertDialog.Builder(requireContext())
            .setView(binding?.root)
            .setCustomTitle(null)
            .create()

    }

    private fun loadImageFromPath(imagePath: String?): Bitmap? {
        return imagePath?.let {
            BitmapFactory.decodeFile(it)
        }
    }

    private fun resizeBitmap(
        bitmap: Bitmap?,
        maxWidth: Int = activity?.windowManager?.currentWindowMetrics?.bounds?.width()?.minus(200) ?: 500,
        maxHeight: Int = activity?.windowManager?.currentWindowMetrics?.bounds?.height()?.minus(200) ?: 700
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