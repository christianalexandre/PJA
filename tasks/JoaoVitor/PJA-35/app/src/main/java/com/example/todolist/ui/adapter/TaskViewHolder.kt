package com.example.todolist.ui.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.todolist.R
import com.example.todolist.ui.database.model.Task

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTextView: TextView? = itemView.findViewById(R.id.titleTextView)
    private val descriptionTextView: TextView? = itemView.findViewById(R.id.descriptionTextView)
    private val imageTask: ImageView? = itemView.findViewById(R.id.picture)
    private val buttonPhoto: ImageView? = itemView.findViewById(R.id.buttonImagePhoto)
    private val checkButton: ImageView? = itemView.findViewById(R.id.checkButton)

    fun bind(task: Task, listener: TaskListener, isFromHome: Boolean) {
        titleTextView?.text = task.title
        descriptionTextView?.text = task.description

        if (!isFromHome) checkButton?.setColorFilter(ContextCompat.getColor(itemView.context, R.color.orange_01))
        if (isFromHome) checkButton?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_uncheck_24dp))

        buttonPhoto?.visibility = View.VISIBLE // Garante que o botão de foto comece visível

        if (!task.image.isNullOrEmpty()) {
            Log.d("TaskViewHolder", "Task ID: ${task.id}, Image Path: ${task.image}")
            imageTask?.let {
                Glide.with(itemView.context)
                    .load(task.image)
                    .placeholder(R.drawable.progress_activity_24dp)
                    .error(R.drawable.no_photography_24dp)
                    .into(it)
            }

            buttonPhoto?.setOnClickListener { view ->
                view.animate()
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .setDuration(100)
                    .withEndAction {
                        view.animate().scaleX(1f).scaleY(1f).setDuration(100)
                    }
                    .start()

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(Uri.parse(task.image), "image/*")
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                try {
                    itemView.context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    buttonPhoto?.visibility = View.GONE
                }
            }
        } else {
            Log.d("TaskViewHolder", "Task ID: ${task.id} has no image.")
            imageTask?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.no_photography_24dp))
            buttonPhoto?.visibility = View.GONE
            buttonPhoto?.setOnClickListener(null)
        }

        // Animação do botão de check
        checkButton?.setOnClickListener { view ->
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
