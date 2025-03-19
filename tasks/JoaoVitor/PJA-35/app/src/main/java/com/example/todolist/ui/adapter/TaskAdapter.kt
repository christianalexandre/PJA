package com.example.todolist.ui.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.TaskItemBinding
import com.example.todolist.ui.database.model.Task

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val listener: TaskListener,
    private val isFromHome: Boolean
) : RecyclerView.Adapter<TaskViewHolder>() {

    private val selectedTasks = mutableSetOf<Task>() // Lista de tarefas selecionadas
    private var isSelectionMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return if (tasks[position].isArchived) 1 else 0
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task, listener, isFromHome, isSelectionMode)

        // Configura o clique no botão de seleção
        holder.itemView.setOnClickListener {
            toggleSelection(task)
        }
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
        selectedTasks.clear() // Limpa seleção ao atualizar
        diffResult.dispatchUpdatesTo(this)
    }

    // Alterna a seleção do item
    fun toggleSelection(task: Task) {
        val position = tasks.indexOf(task)
        notifyItemChanged(position)
    }

    // Limpa a seleção
    @SuppressLint("NotifyDataSetChanged")
    fun clearSelection() {
        tasks.forEach {
            it.isSelected = false
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun selectAll() {
        tasks.forEach {
            it.isSelected = true
        }
        notifyDataSetChanged()
    }

    fun setupSelectionMode(isSelectionMode: Boolean) {
        this.isSelectionMode = isSelectionMode
    }
}
