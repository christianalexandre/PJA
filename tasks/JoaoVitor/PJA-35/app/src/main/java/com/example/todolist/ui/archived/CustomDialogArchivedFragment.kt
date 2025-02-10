package com.example.todolist.ui.archived

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.R

class CustomDialogArchivedFragment (
    private val onDelete: () -> Unit,
    private val onUnarchive: () -> Unit
) : DialogFragment() {
    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.custom_dialog_archived, null)

        val buttonDelete = view.findViewById<Button>(R.id.buttonDelete)
        val buttonArchive = view.findViewById<Button>(R.id.buttonUnarchive)

        buttonDelete.setOnClickListener {
            onDelete.invoke()
            dismiss()
        }

        buttonArchive.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.vp)
            viewPager.currentItem = 0

            onUnarchive.invoke()
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}