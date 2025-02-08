package com.example.todo.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.TaskSingleton
import com.example.todo.room.TaskDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel: ViewModel() {

    private var taskDao: TaskDao? = null
    var isSuccess: MutableLiveData<Boolean> = MutableLiveData(false)

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

                Log.d("RX_DEBUG", "GET ALL TASK: OK")

                TaskSingleton.taskList = it.toMutableList()
                isSuccess.postValue(true)

            }, { error ->
                Log.e("RX_DEBUG", "GET ALL TASKS: ${error.message}")
            })

    }

}