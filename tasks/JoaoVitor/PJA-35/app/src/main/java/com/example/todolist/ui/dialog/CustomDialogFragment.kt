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
    private val isFromHome: Boolean = true,
    private val listener: DialogListener
) : DialogFragment() {

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val binding = CustomDialogBinding.inflate(LayoutInflater.from(requireContext()))

        with(binding) {
            title.text = if (isFromHome) getText(R.string.text_title_dialog) else getText(R.string.text_task_concluded)
            buttonDelete.text = getText(R.string.button_dialog_delete)
            buttonArchive.text = if (isFromHome) getText(R.string.button2_dialog_home) else getText(R.string.text_restauring)

            val icon = if (isFromHome) R.drawable.ic_archived_24dp else R.drawable.ic_unarchive
            val drawable = ContextCompat.getDrawable(requireContext(), icon)

            buttonArchive.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

            buttonDelete.setOnClickListener {
                listener.onSecondPressed()
                dismiss()
            }

            buttonArchive.setOnClickListener {
                val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp)
                viewPager.currentItem = if (isFromHome) 1 else 0
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
        fun checkShowDialog(fragmentManager: FragmentManager, isFromHome: Boolean, listener: DialogListener) {
            if (fragmentManager.findFragmentByTag("CustomDialog") == null) {
                CustomDialogFragment(isFromHome, listener).show(fragmentManager, "CustomDialog")
            }
        }
    }
}
