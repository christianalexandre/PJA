package com.example.todo.modules.main.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.utils.listener.CardActionListener
import com.example.todo.utils.models.Task

class HomeTaskAdapter(
    val taskList: MutableList<Task>,
    private val listener: CardActionListener
): RecyclerView.Adapter<HomeTaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTaskViewHolder {
        return HomeTaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_task, parent, false),
            listener
        )
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: HomeTaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    fun addNewTask() { notifyItemInserted(0) }

    fun removeTask(index: Int) { notifyItemRemoved(index) }

}