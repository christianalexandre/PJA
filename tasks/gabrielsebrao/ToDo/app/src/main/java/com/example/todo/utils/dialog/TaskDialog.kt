package com.example.todo.utils.dialog

import android.app.Dialog
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import com.example.todo.R
import com.example.todo.databinding.ItemDialogTaskCheckBinding
import com.example.todo.utils.listener.TaskActionListener
import com.example.todo.utils.models.Task

class TaskDialog: AppCompatDialogFragment() {

    private var binding: ItemDialogTaskCheckBinding? = null
    private var listener: TaskActionListener? = null

    private var secondButtonTextRes: Int = 0
    private var secondIconRes: Int = 0
    private var secondIconAltTextRes: Int = 0

    companion object {

        const val TAG = "TaskDialog"
        private const val KEY = "TASK"
        private const val KEY_IS_FROM_HOME = "IS_FROM_HOME"

        fun newInstance(task: Task?, listener: TaskActionListener, isFromHome: Boolean): TaskDialog {

            val args = Bundle()
            args.putParcelable(KEY, task)
            args.putBoolean(KEY_IS_FROM_HOME, isFromHome)
            return TaskDialog().apply {
                this.listener = listener
                arguments = args
            }

        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = ItemDialogTaskCheckBinding.inflate(layoutInflater)

        if(arguments?.getBoolean(KEY_IS_FROM_HOME) == true) {
            secondButtonTextRes = R.string.archive_button_text
            secondIconRes = R.drawable.icon_archive
            secondIconAltTextRes = R.string.alt_icon_archived
        } else {
            secondButtonTextRes = R.string.archived_unarchive_button_text
            secondIconRes = R.drawable.icon_unarchive
            secondIconAltTextRes = R.string.alt_icon_unarchived
        }

        setupView()
        setupListeners()

        return AlertDialog.Builder(requireContext())
            .setView(binding?.root)
            .setCustomTitle(null)
            .create()

    }

    private fun setupView() {

        binding?.buttonDeleteText?.setText(R.string.delete_button_text)

        binding?.buttonSecondText?.setText(secondButtonTextRes)

        binding?.iconSecond?.setImageIcon(Icon.createWithResource(context, secondIconRes))
        binding?.iconSecond?.contentDescription = ContextCompat.getString(requireContext(), secondIconAltTextRes)

    }

    private fun setupListeners() {

        binding?.buttonDeleteTask?.setOnClickListener {
            listener?.onDeleteTask(arguments?.getParcelable(KEY))
            dialog?.dismiss()
        }

        binding?.buttonSecond?.setOnClickListener {
            listener?.onSecondAction(arguments?.getParcelable(KEY))
            dialog?.dismiss()
        }

    }

}