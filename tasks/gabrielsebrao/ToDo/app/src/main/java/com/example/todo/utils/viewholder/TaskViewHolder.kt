package com.example.todo.utils.viewholder

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.ItemCardTaskBinding
import com.example.todo.utils.extensions.toSpelledOut
import com.example.todo.utils.listener.CardActionListener
import com.example.todo.utils.models.Task
import java.util.Calendar
import kotlin.coroutines.coroutineContext

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

        if(task?.isArchived == true) {

            binding.iconCheck.setColorFilter(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.green
                )
            )

            binding.title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.content.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        }

        if(task?.image != null)
            binding.buttonAccessImage.visibility = View.VISIBLE
        else
            binding.buttonAccessImage.visibility = View.GONE

        if(task?.conclusionDate != null) {
            binding.textConclusionDate.text =
                task?.conclusionDate?.toSpelledOut(Calendar.getInstance())
            binding.conclusionDate.visibility = View.VISIBLE
        }
        else
            binding.conclusionDate.visibility = View.GONE

        if((task?.conclusionDate ?: 0) <= Calendar.getInstance().timeInMillis) {

            binding.textConclusionDate
                .setTextColor(ContextCompat.getColor(binding.root.context, R.color.error_red))

            binding.iconConclusionDate
                .setColorFilter(ContextCompat.getColor(binding.root.context, R.color.error_red))

        }

    }

    private fun setupListener() {

        val scaleUp = ObjectAnimator.ofFloat(binding.frameLayout, "scaleX", 1f, 1.2f)
        val scaleDown = ObjectAnimator.ofFloat(binding.frameLayout, "scaleX", 1.2f, 1f)

        val scaleUpY = ObjectAnimator.ofFloat(binding.frameLayout, "scaleY", 1f, 1.2f)
        val scaleDownY = ObjectAnimator.ofFloat(binding.frameLayout, "scaleY", 1.2f, 1f)

        scaleUp.duration = 200
        scaleDown.duration = 200
        scaleUpY.duration = 200
        scaleDownY.duration = 200

        val scaleUpDown = AnimatorSet()
        scaleUpDown.play(scaleDown).after(scaleUp)
        scaleUpDown.play(scaleDownY).after(scaleUpY)

        binding.frameLayout.setOnClickListener {

            scaleUp.start()
            scaleUpY.start()
            scaleUpDown.start()

            listener.onCheckClicked(task)
        }

        if(task?.image != null)
            binding.buttonAccessImage.setOnClickListener { listener.onImageCLicked(task) }

    }

}