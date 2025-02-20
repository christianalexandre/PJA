package com.example.todo.utils.task

sealed class TaskState {
    data object Success: TaskState()
    data object Error: TaskState()
}