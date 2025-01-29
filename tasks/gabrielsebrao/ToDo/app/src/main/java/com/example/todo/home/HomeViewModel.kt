package com.example.todo.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.room.Task
import com.example.todo.room.TaskDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel: ViewModel() {

    private var taskDao: TaskDao? = null
    var isSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var taskList: List<Task>? = null

    fun taskDao(taskDao: TaskDao?) = apply {
        this.taskDao = taskDao
    }

    fun getAllTasks(): Disposable {

        return Single.create { emitter ->
            emitter.onSuccess(taskDao?.getAll() ?: emptyList())
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isSuccess.postValue(true)
                taskList = it
                Log.d("RX_DEBUG", "GET ALL TASK: OK")
            }, { error ->
                Log.e("RX_DEBUG", "GET ALL TASKS: ${error.message}")
            })

    }

    fun changeTitleTaskById(title: String, id: Int): Disposable? {

        return taskDao?.changeTitleById(title, id)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                Log.d("RX_DEBUG", "CHANGE TITLE TASK BY ID: OK")
            }, { error ->
                Log.e("RX_DEBUG", "CHANGE TITLE TASK BY ID: ${error.message}")
            })

    }

    fun changeContentTaskById(content: String, id: Int): Disposable? {

        return taskDao?.changeContentById(content, id)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                Log.e("RX_DEBUG", "CHANGE CONTENT TASK BY ID: OK")
            }, { error ->
                Log.e("RX_DEBUG", "CHANGE CONTENT TASK BY ID: ${error.message}")
            })

    }

}