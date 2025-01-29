package com.example.todo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.room.Task

class TaskAdapter(private val taskList: List<Task>): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(task: Task) {

            itemView.findViewById<TextView>(R.id.title).text = task.title
            itemView.findViewById<TextView>(R.id.content).text = task.content

        }

    }


}