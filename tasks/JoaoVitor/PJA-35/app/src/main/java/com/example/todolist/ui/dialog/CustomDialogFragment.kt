package com.example.todolist.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.R
import com.example.todolist.databinding.CustomDialogBinding

class CustomDialogFragment(
    private val origin: DialogOrigin,
    private val listener: DialogListener
) : DialogFragment() {

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val binding = CustomDialogBinding.inflate(LayoutInflater.from(requireContext()))

        with(binding) {
            title.text = when (origin) {
                DialogOrigin.HOME_CARD_CHECK -> getText(R.string.text_title_dialog)
                DialogOrigin.ARCHIEVE_CARD_CHECK -> getText(R.string.text_task_concluded)
                DialogOrigin.PICTURE_DELETE -> getText(R.string.question_dialog_text)
            }
            buttonDelete.text = when (origin) {
                DialogOrigin.HOME_CARD_CHECK -> getText(R.string.button_dialog_delete)
                DialogOrigin.ARCHIEVE_CARD_CHECK -> getText(R.string.button_dialog_delete)
                DialogOrigin.PICTURE_DELETE -> getText(R.string.confirm_text)
            }
            buttonArchive.text = when (origin) {
                DialogOrigin.HOME_CARD_CHECK -> getText(R.string.button2_dialog_home)
                DialogOrigin.ARCHIEVE_CARD_CHECK -> getText(R.string.text_restauring)
                DialogOrigin.PICTURE_DELETE -> getText(R.string.cancel_text)
            }

            val icon = when (origin) {
                DialogOrigin.HOME_CARD_CHECK -> R.drawable.ic_archived_24dp
                DialogOrigin.ARCHIEVE_CARD_CHECK -> R.drawable.ic_unarchive
                DialogOrigin.PICTURE_DELETE -> null
            }

            val iconDelete = when (origin) {
                DialogOrigin.HOME_CARD_CHECK -> R.drawable.ic_delete_24dp
                DialogOrigin.ARCHIEVE_CARD_CHECK -> R.drawable.ic_delete_24dp
                DialogOrigin.PICTURE_DELETE -> null
            }

            val drawable = icon?.let { ContextCompat.getDrawable(requireContext(), it) }
            val drawableDelete = iconDelete?.let { ContextCompat.getDrawable(requireContext(), it) }

            buttonArchive.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            buttonDelete.setCompoundDrawablesWithIntrinsicBounds(drawableDelete, null, null, null)

            buttonDelete.setOnClickListener {
                listener.onSecondPressed()
                dismiss()
            }

            buttonArchive.setOnClickListener {
                val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp)
                viewPager.currentItem = when (origin) {
                    DialogOrigin.HOME_CARD_CHECK -> 0
                    DialogOrigin.ARCHIEVE_CARD_CHECK -> 0
                    DialogOrigin.PICTURE_DELETE -> 2
                }
                listener.onFirstPressed()
                dismiss()
            }

            builder.setView(binding.root)
            return builder.create()
        }
    }

    interface DialogListener {
        fun onFirstPressed()
        fun onSecondPressed()
    }

    companion object {
        fun checkShowDialog(fragmentManager: FragmentManager, dialogOrigin: DialogOrigin, listener: DialogListener) {
            if (fragmentManager.findFragmentByTag("CustomDialog") == null) {
                CustomDialogFragment(dialogOrigin, listener).show(fragmentManager, "CustomDialog")
            }
        }
    }
}
