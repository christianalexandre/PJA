package com.example.todo.task

sealed class TaskState {
    data object Success: TaskState()
    data object Error: TaskState()
}