package com.example.todolist.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.ui.database.model.Task
import com.example.todolist.ui.database.repository.TaskRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel(private val taskDao: TaskRepository) : ViewModel() {
    private val disposables = CompositeDisposable()

    val tasksLiveData: LiveData<List<Task>> = taskDao.getActiveTasksLiveData()

    fun deleteTask(task: Task) {
        val disposable = taskDao.deleteTask(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                println("Tarefa deletada com sucesso!")
            }, { erro ->
                println("Erro ao deletar tarefa: ${erro.message}")
            })
        disposables.add(disposable)
    }

    fun archiveTask(task: Task) {
        val archivedTask = task.copy(isArchived = true, image = task.image)
        val disposable = taskDao.updateTask(archivedTask)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                println("Tarefa arquivada com sucesso!")
            }, { erro ->
                println("Erro ao arquivar tarefa: ${erro.message}")
            })
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
