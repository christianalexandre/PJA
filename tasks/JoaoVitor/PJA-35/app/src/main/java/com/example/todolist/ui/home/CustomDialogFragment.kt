package com.example.todolist.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.todolist.R

class CustomDialogFragment(
    private val onDelete: () -> Unit,
    private val onArchive: () -> Unit
) : DialogFragment() {
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
            onArchive.invoke()
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }
}


