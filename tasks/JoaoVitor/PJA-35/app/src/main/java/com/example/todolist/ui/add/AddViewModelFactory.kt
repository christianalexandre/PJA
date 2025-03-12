package com.example.todolist.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.ui.database.repository.TaskRepository

@Suppress("UNCHECKED_CAST")
class AddViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            return AddViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}