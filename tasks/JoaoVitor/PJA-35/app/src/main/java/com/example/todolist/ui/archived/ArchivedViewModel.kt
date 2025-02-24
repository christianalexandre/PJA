package com.example.todolist.ui.archived

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.ui.database.model.Task
import com.example.todolist.ui.database.repository.TaskRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ArchivedViewModel(private val repository: TaskRepository) : ViewModel() {
    private val disposables = CompositeDisposable()

    val archivedTasksLiveData: LiveData<List<Task>> = repository.getArchivedTasksLiveData()

    fun deleteTask(task: Task) {
        val disposable = repository.deleteTask(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                println("Tarefa deletada com sucesso!")
            }, { erro ->
                println("Erro ao deletar tarefa: ${erro.message}")
            })
        disposables.add(disposable)
    }

    fun unarchiveTask(task: Task) {
        val unarchivedTask = task.copy(isArchived = false, image = task.image)
        val disposable = repository.updateTask(unarchivedTask)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                println("Tarefa desarquivada com sucesso!")
            }, { erro ->
                println("Erro ao desarquivar tarefa: ${erro.message}")
            })
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
