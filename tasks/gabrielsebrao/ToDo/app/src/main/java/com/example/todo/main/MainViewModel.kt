package com.example.todo.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todo.task.TaskSingleton
import com.example.todo.room.DataBase
import com.example.todo.task.Task
import com.example.todo.task.TaskDao
import com.example.todo.sharedpref.ToDoSharedPref
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.ref.WeakReference

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val contextRef: WeakReference<Context> = WeakReference(application.applicationContext)
    private val sharedPref: ToDoSharedPref? = ToDoSharedPref.getInstance(contextRef.get())
    private val taskDao: TaskDao? = DataBase.getInstance(contextRef.get())?.taskDao()

    val isGetAllTasksSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    val isArchiveTaskSuccess: MutableLiveData<Boolean> = MutableLiveData(false)

    private var archivedTask: Task? = null

    var archivedItemIndex: Int = 0

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

    fun archiveTask(task: Task?): Disposable? {

        if(task == null)
            return null

        return Single.create { emitter ->
            emitter.onSuccess(taskDao?.changeIsArchivedById(task.id, true) ?: emitter.onError(NullPointerException()))
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                task.isArchived = true

                if(TaskSingleton.archivedTaskIdList == null)
                    sharedPref?.saveArchivedTaskIdList(mutableListOf(task.id))
                else
                    sharedPref?.addArchivedTaskIdToList(task.id)

                if(TaskSingleton.archivedTaskList == null)
                    TaskSingleton.archivedTaskList = mutableListOf(task)
                else
                    TaskSingleton.archivedTaskList?.add(0, task)

                archivedTask = TaskSingleton.openTaskList?.find { it.isArchived } ?: return@subscribe
                archivedItemIndex = TaskSingleton.openTaskList?.indexOf(archivedTask) ?: return@subscribe

                TaskSingleton.openTaskList?.remove(task)
                sharedPref?.removeOpenTaskId(task.id)


                TaskSingleton.archivedTask = task

                Log.d("RX_DEBUG", "ARCHIVE TASK: OK")
                Log.d("ROOM_DEBUG", "ARCHIVED TASK ID LIST: ${TaskSingleton.archivedTaskIdList}")
                Log.d("ROOM_DEBUG", "ARCHIVED TASK LIST: ${TaskSingleton.archivedTaskList}")
                isArchiveTaskSuccess.postValue(true)

            }, { error ->
                Log.e("RX_DEBUG", "ARCHIVE TASK: ${error.message}")
            })

    }

}