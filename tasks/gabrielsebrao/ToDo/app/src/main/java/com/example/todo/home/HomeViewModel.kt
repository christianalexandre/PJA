package com.example.todo.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.TaskSingleton
import com.example.todo.room.Task
import com.example.todo.room.TaskDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel: ViewModel() {

    private var taskDao: TaskDao? = null
    var isGetAllTasksSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var isDeleteTaskSuccess: MutableLiveData<Boolean> = MutableLiveData(false)

    fun taskDao(taskDao: TaskDao?) = apply {
        this.taskDao = taskDao
    }

    fun getAllTasks(): Disposable {

        return Single.create { emitter ->
            emitter.onSuccess(taskDao?.getAll() ?: emptyList())
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ taskList ->

                Log.d("RX_DEBUG", "GET ALL TASK: OK")

                TaskSingleton.openTaskList = taskList.filter { !it.isArchived }.toMutableList()
                TaskSingleton.archivedTaskList = taskList.filter { it.isArchived }.toMutableList()

                isGetAllTasksSuccess.postValue(true)

            }, { error ->
                Log.e("RX_DEBUG", "GET ALL TASKS: ${error.message}")
            })

    }

    fun deleteTask(task: Task): Disposable {

        return Single.create { emitter ->
            emitter.onSuccess(taskDao?.deleteTaskById(task.id) ?: emitter.onError(NullPointerException()))
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                Log.d("RX_DEBUG", "DELETE TASK: OK")
                Log.d("ROOM_DEBUG", "TASK TO BE DELETED: $task")

                TaskSingleton.deletedTaskId = task.id
                isDeleteTaskSuccess.postValue(true)

            }, { error ->
                Log.e("RX_DEBUG", "DELETE TASK: ${error.message}")
            })

    }

}