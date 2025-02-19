package com.example.todo.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todo.task.TaskSingleton
import com.example.todo.room.DataBase
import com.example.todo.task.Task
import com.example.todo.task.TaskDao
import com.example.todo.sharedpref.ToDoSharedPref
import com.example.todo.task.TaskState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.ref.WeakReference

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val contextRef: WeakReference<Context> = WeakReference(application.applicationContext)
    private val sharedPref: ToDoSharedPref? = ToDoSharedPref.getInstance(contextRef.get())
    private val taskDao: TaskDao? = DataBase.getInstance(contextRef.get())?.taskDao()

    private val _addTaskState: MutableLiveData<TaskState?> = MutableLiveData()
    private val _getTasksState: MutableLiveData<TaskState?> = MutableLiveData()
    private val _archiveTaskState: MutableLiveData<TaskState?> = MutableLiveData()
    private val _unarchiveTaskState: MutableLiveData<TaskState?> = MutableLiveData()

    val getTasksState: LiveData<TaskState?> = _getTasksState
    val addTaskState: LiveData<TaskState?> = _addTaskState
    val archiveTaskState: LiveData<TaskState?> = _archiveTaskState
    val unarchiveTaskState: LiveData<TaskState?> = _unarchiveTaskState

    private var archivedTask: Task? = null
    private var unarchivedTask: Task? = null

    var archivedItemIndex: Int = 0
    var unarchivedItemIndex: Int = 0

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

                TaskSingleton.archivedTaskList = TaskSingleton.archivedTaskList?.sortedBy { task ->
                    TaskSingleton.archivedTaskIdList?.indexOf(task.id)
                }?.toMutableList()

                Log.e("ROOM_DEBUG", "${TaskSingleton.openTaskList}")
                Log.d("RX_DEBUG", "GET ALL TASK: OK")
                _getTasksState.postValue(TaskState.Success)

            }, { error ->
                _getTasksState.postValue(TaskState.Error)
                Log.e("RX_DEBUG", "GET ALL TASKS: ${error.message}")
            })

    }

    fun addTask(title: String, content: String): Disposable? {

        val task: Task?

        try {
            task = Task(sharedPref?.nextTaskId ?: 0, title, content, false)
        } catch(error: Exception) {
            _addTaskState.value = TaskState.Error
            Log.e("ROOM_DEBUG", "ADD TASK: ${error.message}")
            return null
        }

        if(TaskSingleton.openTaskIdList == null)
            sharedPref?.saveOpenTaskIdList(mutableListOf(sharedPref.nextTaskId))
        else
            sharedPref?.addOpenTaskIdToList(sharedPref.nextTaskId)

        return taskDao?.insertAll(task)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({

                Log.d("RX_DEBUG", "ADD TASK: OK")
                Log.d("RX_DEBUG", "LIST FROM SHARED PREF: ${TaskSingleton.openTaskIdList}")
                Log.d("RX_DEBUG", "CURRENT TASK ID: ${sharedPref?.nextTaskId}")

                sharedPref?.incrementCurrentTaskId()
                TaskSingleton.newTask = task
                TaskSingleton.openTaskList?.add(0, task)

                Log.d("RX_DEBUG", "${TaskSingleton.openTaskList}")

                _addTaskState.postValue(TaskState.Success)

            }, { error ->
                _addTaskState.postValue(TaskState.Error)
                Log.e("RX_DEBUG", "ADD TASK: ${error.message}")
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
                _archiveTaskState.postValue(TaskState.Success)

            }, { error ->
                _archiveTaskState.postValue(TaskState.Error)
                Log.e("RX_DEBUG", "ARCHIVE TASK: ${error.message}")
            })

    }

    fun unarchiveTask(task: Task?): Disposable? {

        if(task == null)
            return null

        return Single.create { emitter->
            emitter.onSuccess(taskDao?.changeIsArchivedById(task.id, false) ?: emitter.onError(NullPointerException()))
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                task.isArchived = false

                if(TaskSingleton.openTaskList == null)
                    TaskSingleton.openTaskList = mutableListOf(task)
                else
                    TaskSingleton.openTaskList?.add(0, task)

                if(TaskSingleton.openTaskIdList == null)
                    sharedPref?.saveOpenTaskIdList(mutableListOf(task.id))
                else
                    sharedPref?.addOpenTaskIdToList(task.id)

                unarchivedTask = TaskSingleton.archivedTaskList?.find { !it.isArchived } ?: return@subscribe
                unarchivedItemIndex = TaskSingleton.archivedTaskList?.indexOf(unarchivedTask) ?: return@subscribe

                TaskSingleton.archivedTaskList?.remove(task)
                sharedPref?.removeArchivedTaskId(task.id)

                TaskSingleton.unarchivedTask = task

                Log.e("RX_DEBUG", "UNARCHIVE TASK: OK")
                _unarchiveTaskState.postValue(TaskState.Success)

            }, { error ->
                _unarchiveTaskState.postValue(TaskState.Error)
                Log.e("RX_DEBUG", "UNARCHIVE TASK: ${error.message}")
            })
    }

}