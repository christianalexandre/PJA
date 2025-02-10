package com.example.todolist.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.ui.database.model.Task
import com.example.todolist.ui.database.dao.TaskDao

class HomeViewModel(taskDao: TaskDao) : ViewModel() {

    val tasksLiveData: LiveData<List<Task>> = taskDao.getAllTasksLiveData()

}
