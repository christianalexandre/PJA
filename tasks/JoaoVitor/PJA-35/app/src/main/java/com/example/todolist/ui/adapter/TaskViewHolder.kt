package com.example.todolist.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.ui.database.model.Task

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
    private val checkButtonHome: ImageView? = itemView.findViewById(R.id.checkButton)

    fun bind(task: Task, listener: TaskListener, isFromHome: Boolean) {
        titleTextView.text = task.title
        descriptionTextView.text = task.description

        if (!isFromHome) checkButtonHome?.setColorFilter(ContextCompat.getColor(itemView.context, R.color.orange_01))

        checkButtonHome?.setOnClickListener { view ->
                view.animate()
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .setDuration(100)
                    .withEndAction {
                        view.animate().scaleX(1f).scaleY(1f).setDuration(100)
                    }
                    .start()

            listener.onCheckPressed(task)
        }
    }
}

