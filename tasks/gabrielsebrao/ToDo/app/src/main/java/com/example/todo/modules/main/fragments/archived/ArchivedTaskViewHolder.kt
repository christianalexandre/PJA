package com.example.todo.modules.main.fragments.archived

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.ItemCardTaskBinding
import com.example.todo.utils.listener.CardActionListener
import com.example.todo.utils.models.Task

class ArchivedTaskViewHolder(itemView: View, private val listener: CardActionListener) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemCardTaskBinding = ItemCardTaskBinding.bind(itemView)
    private var dialog: AlertDialog? = null
    private var dialogView: View? = null
    private var task: Task? = null

    fun bind(task: Task) {

        this.task = task

        dialogView = LayoutInflater.from(binding.root.context)
            .inflate(R.layout.item_dialog_task_check, binding.root.parent as ViewGroup?, false)

        dialog = AlertDialog.Builder(binding.root.context)
            .setCustomTitle(null)
            .setView(dialogView)
            .create()

        setupView()
        setupListener()

    }

    private fun setupView() {

        binding.title.text = task?.title
        binding.content.text = task?.content

        if(task?.image?.isEmpty() != true)
            binding.buttonAccessImage.visibility = View.VISIBLE

    }

    private fun setupListener() {

        binding.frameLayout.setOnClickListener { listener.onCheckClicked(task) }

        if(task?.image?.isEmpty() != true)
            binding.buttonAccessImage.setOnClickListener { listener.onImageCLicked(task) }

    }

}