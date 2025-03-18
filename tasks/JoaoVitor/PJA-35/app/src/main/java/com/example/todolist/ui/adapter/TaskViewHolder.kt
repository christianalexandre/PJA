package com.example.todolist.ui.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.todolist.R
import com.example.todolist.databinding.TaskItemBinding
import com.example.todolist.ui.database.model.Task

class TaskViewHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {

    private val imageTask: ImageView? = itemView.findViewById(R.id.picture)


    fun bind(task: Task, listener: TaskListener, isFromHome: Boolean) {
        binding.titleTextView?.text = task.title
        binding.descriptionTextView?.text = task.description
        binding.datePickerTextView?.text = task.date

        if (!isFromHome || task.isSelected) {
            binding.checkButton?.setColorFilter(ContextCompat.getColor(itemView.context, R.color.orange_01))
            binding.checkButton?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_check_circle))
        } else {
            binding.checkButton?.setColorFilter(ContextCompat.getColor(itemView.context, R.color.white))
            binding.checkButton?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_uncheck_24dp))
        }

        binding.buttonImagePhoto?.visibility = View.VISIBLE // Garante que o botão de foto comece visível

        if (!task.image.isNullOrEmpty()) {
            Log.d("TaskViewHolder", "Task ID: ${task.id}, Image Path: ${task.image}")
            imageTask?.let {
                Glide.with(itemView.context)
                    .load(task.image)
                    .placeholder(R.drawable.progress_activity_24dp)
                    .error(R.drawable.no_photography_24dp)
                    .into(it)
            }

            binding.buttonImagePhoto?.setOnClickListener { view ->
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
                    binding.buttonImagePhoto.visibility = View.GONE
                }
            }
        } else {
            Log.d("TaskViewHolder", "Task ID: ${task.id} has no image.")
            imageTask?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.no_photography_24dp))
            binding.buttonImagePhoto?.visibility = View.GONE
            binding.buttonImagePhoto?.setOnClickListener(null)
        }

        binding.cardItem.setOnClickListener {
                view ->
            view.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(100)
                .withEndAction {
                    view.animate().scaleX(1f).scaleY(1f).setDuration(100)
                }
                .start()

            task.isSelected = !task.isSelected
            listener.onCheckPressed(task)
        }

        // Animação do botão de check
        binding.checkButton.setOnClickListener { view ->
            view.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(100)
                .withEndAction {
                    view.animate().scaleX(1f).scaleY(1f).setDuration(100)
                }
                .start()

            task.isSelected = !task.isSelected
            listener.onCheckPressed(task)
        }
    }
}
