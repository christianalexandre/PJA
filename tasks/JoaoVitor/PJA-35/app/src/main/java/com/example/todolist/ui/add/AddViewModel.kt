package com.example.todolist.ui.add

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.ui.database.model.Task
import com.example.todolist.ui.database.repository.TaskRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddViewModel(private val repository: TaskRepository) : ViewModel() {

    private val disposables = CompositeDisposable()

    // LiveData para observar as tarefas no banco nao arquivadas
    val todosLiveData: LiveData<List<Task>> = repository.getAllTasksLiveData()

    @SuppressLint("CheckResult")
    fun insertTask(task: Task) {
        val disposable = repository.insertTask(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                println("Tarefa inserida com sucesso!")
            }, { erro ->
                println("Erro ao inserir tarefa: ${erro.message}")
            })
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear() // Libera os recursos do RxJava quando o ViewModel for destruído
    }
}
