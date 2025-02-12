package com.example.todolist.ui.archived

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.ui.database.repository.TaskRepository

class ArchivedViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArchivedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArchivedViewModel(repository) as T
        }
        throw IllegalArgumentException("View model desconhecido")
    }
}
