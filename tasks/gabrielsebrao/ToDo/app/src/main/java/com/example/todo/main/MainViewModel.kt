package com.example.todo.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.TaskSingleton
import com.example.todo.room.TaskDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel: ViewModel() {

    private var taskDao: TaskDao? = null
    val isGetAllTasksSuccess: MutableLiveData<Boolean> = MutableLiveData(false)

    fun taskDao(taskDao: TaskDao?) = apply { this.taskDao = taskDao }

    fun getAllTasks(): Disposable {

        return Single.create { emitter ->
            emitter.onSuccess(taskDao?.getAll() ?: emptyList())
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ taskList ->

                TaskSingleton.openTaskList = taskList.filter { !it.isArchived }.toMutableList()
                TaskSingleton.archivedTaskList = taskList.filter { it.isArchived }.toMutableList()

                TaskSingleton.openTaskList = TaskSingleton.openTaskList?.sortedBy { task ->
                    TaskSingleton.openTaskIdList?.indexOf(task.id)
                }?.toMutableList()

                Log.e("ROOM_DEBUG", "${TaskSingleton.openTaskList}")
                Log.d("RX_DEBUG", "GET ALL TASK: OK")
                isGetAllTasksSuccess.postValue(true)

            }, { error ->
                Log.e("RX_DEBUG", "GET ALL TASKS: ${error.message}")
            })

    }

}