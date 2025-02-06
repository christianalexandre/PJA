package com.example.todolist.ui.archived

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.ui.database.dao.TaskDao

class ArchivedViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArchivedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArchivedViewModel(taskDao) as T
        }
        throw IllegalArgumentException("View model desconhecido")
    }
}
