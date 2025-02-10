package com.example.todolist.ui.add

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.ui.database.dao.TaskDao
import com.example.todolist.ui.database.model.Task
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddViewModel(private val taskDao: TaskDao) : ViewModel() {

    private val disposables = CompositeDisposable()

    // LiveData para observar as tarefas no banco nao arquivadas
    val todosLiveData: LiveData<List<Task>> = taskDao.getAllTasksLiveData()

    // Inserir uma nova tarefa
    @SuppressLint("CheckResult")
    fun inserirTask(task: Task) {
        val disposable = taskDao.inserirTask(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                println("Tarefa inserida com sucesso!")
            }, { erro ->
                println("Erro ao inserir tarefa: ${erro.message}")
            })
        disposables.add(disposable)
    }

    // Atualizar uma tarefa
    @SuppressLint("CheckResult")
    fun atualizarTask(task: Task) {
        val disposable = taskDao.atualizarTask(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                println("Tarefa atualizada com sucesso!")
            }, { erro ->
                println("Erro ao atualizar tarefa: ${erro.message}")
            })
        disposables.add(disposable)
    }

    // Deletar uma tarefa
    @SuppressLint("CheckResult")
    fun deletarTask(task: Task) {
        val disposable = taskDao.deletarTask(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                println("Tarefa deletada com sucesso!")
            }, { erro ->
                println("Erro ao deletar tarefa: ${erro.message}")
            })
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear() // Libera os recursos do RxJava quando o ViewModel for destruído
    }
}
