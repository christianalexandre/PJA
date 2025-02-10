package com.example.todolist.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.R

class CustomDialogFragment(
    private val onDelete: () -> Unit,
    private val onArchive: () -> Unit
) : DialogFragment() {
    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.custom_dialog, null)

        val buttonDelete = view.findViewById<Button>(R.id.buttonDelete)
        val buttonArchive = view.findViewById<Button>(R.id.buttonArchive)

        buttonDelete.setOnClickListener {
            onDelete.invoke()
            dismiss()
        }

        buttonArchive.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp)
            viewPager.currentItem = 1

            onArchive.invoke()
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}


