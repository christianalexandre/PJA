package com.example.todo.task

import com.example.todo.task.Task

interface TaskActionListener {
    fun onDeleteTask(task: Task?)
    fun onArchiveTask(task: Task?)
}