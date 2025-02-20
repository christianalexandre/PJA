package com.example.todo.utils.listener

import com.example.todo.utils.models.Task

interface TaskActionListener {
    fun onDeleteTask(task: Task?)
    fun onArchiveTask(task: Task?)
    fun onUnarchiveTask(task: Task?)
}