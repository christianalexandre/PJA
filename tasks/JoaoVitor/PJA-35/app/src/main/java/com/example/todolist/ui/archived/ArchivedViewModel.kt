package com.example.todolist.ui.archived

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.ui.database.dao.TaskDao
import com.example.todolist.ui.database.model.Task

class ArchivedViewModel(private val taskDao: TaskDao) : ViewModel() {
    val archivedTasksLiveData: LiveData<List<Task>> = taskDao.getArchivedTasksLiveData()
}
