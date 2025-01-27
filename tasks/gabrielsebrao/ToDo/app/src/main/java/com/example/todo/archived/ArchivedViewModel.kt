package com.example.todo.archived

import androidx.lifecycle.ViewModel
import com.example.todo.room.DataBase
import com.example.todo.room.TaskDao

class ArchivedViewModel: ViewModel() {

    private var db: DataBase? = null
    private var taskDao: TaskDao? = null

    fun taskDao(taskDao: TaskDao?) = apply {
        this.taskDao = taskDao
    }
}