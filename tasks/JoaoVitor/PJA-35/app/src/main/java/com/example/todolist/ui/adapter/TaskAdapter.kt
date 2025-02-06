package com.example.todolist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.ui.database.model.Task
import com.example.todolist.ui.home.CustomDialogFragment
import com.google.android.material.checkbox.MaterialCheckBox

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onDeleteTask: (Task) -> Unit,
    private val onArchiveTask: (Task) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ACTIVE = 0
        private const val VIEW_TYPE_ARCHIVED = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (tasks[position].isArchived) VIEW_TYPE_ARCHIVED else VIEW_TYPE_ACTIVE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = if (viewType == VIEW_TYPE_ARCHIVED) {
            R.layout.archived_item  // Layout diferente para arquivadas
        } else {
            R.layout.task_item
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = tasks[position]
        (holder as TaskViewHolder).bind(task, position)
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    private fun removeTask(position: Int, task: Task) {
        if (position in tasks.indices) {
            tasks.removeAt(position)
            notifyItemRemoved(position)
            onDeleteTask(task)
        }
    }

    private fun archiveTask(position: Int, task: Task) {
        if (position in tasks.indices) {
            tasks.removeAt(position)
            notifyItemRemoved(position)
            onArchiveTask(task)
        }
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val checkBox: MaterialCheckBox? = itemView.findViewById(R.id.checkBox)

        fun bind(task: Task, position: Int) {
            titleTextView.text = task.title
            descriptionTextView.text = task.description

            checkBox?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val activity = itemView.context as? AppCompatActivity
                    activity?.let {
                        CustomDialogFragment(
                            onDelete = { removeTask(position, task) },
                            onArchive = { archiveTask(position, task) }
                        ).show(it.supportFragmentManager, "CustomDialogFragment")
                    }
                    checkBox.isChecked = false
                }
            }
        }
    }
}
