package com.example.todo.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.home.HomeViewModel
import com.example.todo.room.Task

class TaskAdapter(val taskList: MutableList<Task>, val homeViewModel: HomeViewModel?): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    fun addNewTask(task: Task?) {

        if(task == null)
            return

        notifyItemInserted(0)

    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var dialog: AlertDialog? = null
        private var dialogView: View? = null
        private var task: Task? = null

        fun bind(task: Task) {

            this.task = task

            itemView.findViewById<TextView>(R.id.title).text = task.title
            itemView.findViewById<TextView>(R.id.content).text = task.content

            setupListener()

        }

        private fun setupListener() {

            setupCheckIconListener()
            setupDeleteTaskButton()

        }

        private fun setupCheckIconListener() {

            dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.item_dialog_delete_or_archive_task, itemView.parent as ViewGroup?, false)

            dialog = AlertDialog.Builder(itemView.context)
                .setCustomTitle(itemView.findViewById(R.id.titleDialog))
                .setView(dialogView)
                .create()

            itemView.findViewById<ImageView>(R.id.icon_check).setOnClickListener { dialog?.show() }

        }

        private fun setupDeleteTaskButton() {

            dialogView?.findViewById<View>(R.id.button_delete_task)?.setOnClickListener {

                homeViewModel?.deleteTask(task ?: Task(0, "error", "error", false))
                dialog?.hide()

            }

        }

    }


}