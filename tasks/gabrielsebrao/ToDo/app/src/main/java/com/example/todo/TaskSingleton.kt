package com.example.todo

import com.example.todo.room.Task

object TaskSingleton {
    var taskStack: MutableList<Task>? = null
    var newTask: Task? = null
}