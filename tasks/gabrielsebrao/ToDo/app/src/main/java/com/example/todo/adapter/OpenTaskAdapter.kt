package com.example.todo.adapter

import android.app.AlertDialog
import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.TaskActionListener
import com.example.todo.room.Task

class OpenTaskAdapter(
    val taskList: MutableList<Task>,
    private val listener: TaskActionListener
): RecyclerView.Adapter<OpenTaskAdapter.TaskViewHolder>() {

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

            dialogView = LayoutInflater.from(itemView.context)
                .inflate(R.layout.item_dialog_task_check, itemView.parent as ViewGroup?, false)

            setupView()
            setupDialogView()
            setupListener()

        }

        private fun setupView() {

            itemView.findViewById<TextView>(R.id.title).text = task?.title
            itemView.findViewById<TextView>(R.id.content).text = task?.content

        }

        private fun setupDialogView() {

            dialogView?.findViewById<TextView>(R.id.button_delete_text)
                ?.setText(R.string.delete_button_text)

            dialogView?.findViewById<TextView>(R.id.button_second_text)
                ?.setText(R.string.archive_button_text)

            dialogView?.findViewById<ImageView>(R.id.icon_second)
                ?.setImageIcon(Icon.createWithResource(itemView.context, R.drawable.icon_archive))

            dialogView?.findViewById<ImageView>(R.id.icon_second)
                ?.contentDescription = ContextCompat.getString(itemView.context, R.string.alt_icon_archived)

        }

        private fun setupListener() {

            setupCheckIconListener()
            setupDeleteTaskButton()
            setupArchiveTaskButton()

        }

        private fun setupCheckIconListener() {

            dialog = AlertDialog.Builder(itemView.context)
                .setCustomTitle(itemView.findViewById(R.id.titleDialog))
                .setView(dialogView)
                .create()

            itemView.findViewById<ImageView>(R.id.icon_check).setOnClickListener { dialog?.show() }

        }

        private fun setupDeleteTaskButton() {

            dialogView?.findViewById<View>(R.id.button_delete_task)?.setOnClickListener {

                listener.onDeleteTask(task)
                dialog?.hide()

            }

        }

        private fun setupArchiveTaskButton() {

            dialogView?.findViewById<View>(R.id.button_second)?.setOnClickListener {

                listener.onArchiveTask(task)
                dialog?.hide()

            }

        }

    }

}