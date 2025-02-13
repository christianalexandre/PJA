package com.example.todolist.ui.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.R

class CustomDialogFragment (
    private val isFromHome: Boolean = true,
    private val listener: DialogListener
) : DialogFragment() {

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.custom_dialog, null)

        val title = view.findViewById<TextView>(R.id.title)
        val secondButton = view.findViewById<Button>(R.id.buttonDelete)
        val firstButton = view.findViewById<Button>(R.id.buttonArchive)

        title.text = if (isFromHome) getText(R.string.text_title_dialog) else getText(R.string.text_task_concluded)
        secondButton.text = if (isFromHome) getText(R.string.button_dialog_delete) else getText(R.string.button_dialog_delete)
        firstButton.text = if (isFromHome) getText(R.string.button2_dialog_home) else getText(R.string.text_restauring)

        val icon = if (isFromHome) R.drawable.ic_archived_24dp else R.drawable.ic_unarchive
        val drawable = ContextCompat.getDrawable(requireContext(), icon)
        firstButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

        secondButton.setOnClickListener {
            listener.onSecondPressed()
            dismiss()
        }

        firstButton.setOnClickListener {
            if (isFromHome) {
                val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp)
                viewPager.currentItem = 1
            } else {
                val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp)
                viewPager.currentItem = 0
            }
            listener.onFirstPressed()
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }

    interface DialogListener {
        fun onFirstPressed()
        fun onSecondPressed()
    }
}