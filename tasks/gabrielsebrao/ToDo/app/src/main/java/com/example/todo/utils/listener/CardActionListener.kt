package com.example.todo.utils.listener

import com.example.todo.utils.models.Task

interface CardActionListener {
    fun onCheckClicked(task: Task?)
}