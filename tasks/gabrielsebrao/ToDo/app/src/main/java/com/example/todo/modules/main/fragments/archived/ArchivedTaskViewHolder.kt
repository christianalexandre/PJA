package com.example.todo.modules.main.fragments.archived

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.utils.listener.CardActionListener
import com.example.todo.utils.models.Task

class ArchivedTaskViewHolder(itemView: View, private val listener: CardActionListener) : RecyclerView.ViewHolder(itemView) {

    private var dialog: AlertDialog? = null
    private var dialogView: View? = null
    private var task: Task? = null

    fun bind(task: Task) {

        this.task = task

        dialogView = LayoutInflater.from(itemView.context)
            .inflate(R.layout.item_dialog_task_check, itemView.parent as ViewGroup?, false)

        dialog = AlertDialog.Builder(itemView.context)
            .setCustomTitle(itemView.findViewById(R.id.titleDialog))
            .setView(dialogView)
            .create()

        setupView()
        setupListener()

    }

    private fun setupView() {

        itemView.findViewById<TextView>(R.id.title).text = task?.title
        itemView.findViewById<TextView>(R.id.content).text = task?.content

        itemView.findViewById<ImageView>(R.id.icon_check)
            ?.setColorFilter(ContextCompat.getColor(itemView.context, R.color.green))

    }

    private fun setupListener() {

        itemView.findViewById<FrameLayout>(R.id.frame_layout).setOnClickListener { listener.onCheckClicked(task) }

    }

}