package com.example.todo

import com.example.todo.room.Task

interface TaskActionListener {
    fun onDeleteTask(task: Task?)
    fun onArchiveTask(task: Task?)
}