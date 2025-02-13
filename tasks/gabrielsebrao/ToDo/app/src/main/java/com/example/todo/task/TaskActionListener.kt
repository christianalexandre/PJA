package com.example.todo.task

interface TaskActionListener {
    fun onDeleteTask(task: Task?)
    fun onArchiveTask(task: Task?)
    fun onUnarchiveTask(task: Task?)
}