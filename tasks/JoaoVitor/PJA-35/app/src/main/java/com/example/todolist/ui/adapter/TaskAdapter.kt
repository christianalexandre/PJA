package com.example.todolist.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.ui.database.model.Task

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val listener: TaskListener,
    private val isFromHome: Boolean
) : RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (tasks[position].isArchived) 1 else 0
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position], listener, isFromHome)
    }

    override fun getItemCount(): Int = tasks.size

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTasks(newTasks: List<Task>) {
        val sortedTasks = newTasks.sortedBy { it.date.toLocalDateTime() } // Ordena por data e hora

        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = tasks.size
            override fun getNewListSize(): Int = sortedTasks.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return tasks[oldItemPosition].id == sortedTasks[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return tasks[oldItemPosition] == sortedTasks[newItemPosition]
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        tasks.clear()
        tasks.addAll(sortedTasks)
        diffResult.dispatchUpdatesTo(this)
    }

}
