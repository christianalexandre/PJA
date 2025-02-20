package com.example.todo.utils.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.todo.R
import com.example.todo.databinding.ItemDialogTaskCheckBinding

class TaskDialog: AppCompatDialogFragment() {

    var binding: ItemDialogTaskCheckBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = ItemDialogTaskCheckBinding.inflate(layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setView(LayoutInflater.from(context).inflate(R.layout.item_dialog_task_check, binding?.root))
            .setCustomTitle(binding?.titleDialog)
            .create()

    }

}