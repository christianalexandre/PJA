package com.example.todolist.ui.adapter

import com.example.todolist.ui.database.model.Task

interface TaskListener {
    fun onCheckPressed(task: Task)
}
