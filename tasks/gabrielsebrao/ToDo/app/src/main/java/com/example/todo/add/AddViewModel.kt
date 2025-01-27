package com.example.todo.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.room.Task
import com.example.todo.room.TaskDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddViewModel: ViewModel() {

    private var taskDao: TaskDao? = null

    fun taskDao(taskDao: TaskDao?) = apply {
        this.taskDao = taskDao
    }

    fun addTask(title: String, content: String): Disposable? {

        return taskDao?.insertAll(
            Task(null,
                title,
                content
            )
        )
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                Log.d("RX_DEBUG", "ADD TASK: OK")
            }, { error ->
                Log.e("RX_DEBUG", "ADD TASK: ${error.message}")
            })

    }

}