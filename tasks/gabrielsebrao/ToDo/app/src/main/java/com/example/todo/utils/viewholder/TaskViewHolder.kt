package com.example.todo.utils.viewholder

import android.app.AlertDialog
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.ItemCardTaskBinding
import com.example.todo.utils.converter.Converter
import com.example.todo.utils.converter.Converter.toDate
import com.example.todo.utils.listener.CardActionListener
import com.example.todo.utils.models.Task
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

class TaskViewHolder(itemView: View, private val listener: CardActionListener) : RecyclerView.ViewHolder(itemView) {

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

        if(task?.image != null)
            binding.buttonAccessImage.visibility = View.VISIBLE
        else
            binding.buttonAccessImage.visibility = View.GONE

        if(task?.conclusionDate != null) {
            binding.textConclusionDate.text =
                Converter.turnTimeStampToSpelledOut(task?.conclusionDate ?: 0, Calendar.getInstance())
            binding.conclusionDate.visibility = View.VISIBLE
        }
        else
            binding.conclusionDate.visibility = View.GONE

        if((task?.conclusionDate ?: return) <= Calendar.getInstance().timeInMillis) {

            binding.textConclusionDate
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.error_red))

            binding.iconConclusionDate
                .setColorFilter(ContextCompat.getColor(binding.root.context, R.color.error_red))

        }

    }

    private fun setupListener() {

        binding.frameLayout.setOnClickListener { listener.onCheckClicked(task) }

        if(task?.image != null)
            binding.buttonAccessImage.setOnClickListener { listener.onImageCLicked(task) }

    }

}