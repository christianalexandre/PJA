package com.example.todolist.ui.database.repository

import androidx.lifecycle.LiveData
import com.example.todolist.ui.database.dao.TaskDao
import com.example.todolist.ui.database.model.Task
import io.reactivex.rxjava3.core.Completable

class TaskRepository(private val taskDao: TaskDao) {

    // Obtém todas as tarefas ativas (não arquivadas)
    fun getActiveTasksLiveData(): LiveData<List<Task>> {
        return taskDao.getActiveTasksLiveData()
    }

    // Obtém todas as tarefas arquivadas
    fun getArchivedTasksLiveData(): LiveData<List<Task>> {
        return taskDao.getArchivedTasksLiveData()
    }

    fun getAllTasksLiveData(): LiveData<List<Task>> {
        return taskDao.getAllTasksLiveData()
    }

    // Insere uma nova tarefa
    fun insertTask(task: Task): Completable {
        return taskDao.insertTask(task)
    }

    // Atualiza uma tarefa (por exemplo, arquivá-la)
    fun updateTask(task: Task): Completable {
        return taskDao.atualizarTask(task)
    }

    // Deleta uma tarefa
    fun deleteTask(task: Task): Completable {
        return taskDao.deletarTask(task)
    }
}

