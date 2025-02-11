package com.example.todo.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.TaskSingleton
import com.example.todo.room.Task
import com.example.todo.room.TaskDao
import com.example.todo.sharedpref.ToDoSharedPref
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel: ViewModel() {

    private var taskDao: TaskDao? = null

    private var removedTask: Task? = null
    private var archivedTask: Task? = null

    var removedItemIndex: Int = 0
    var archivedItemIndex: Int = 0

    var isGetAllTasksSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var isDeleteTaskSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var isArchiveTaskSuccess: MutableLiveData<Boolean> = MutableLiveData(false)

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

    fun deleteTask(task: Task): Disposable {

        return Single.create { emitter ->
            emitter.onSuccess(taskDao?.deleteTaskById(task.id) ?: emitter.onError(NullPointerException()))
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                TaskSingleton.deletedTaskId = task.id

                removedTask = TaskSingleton.openTaskList?.find { it.id == TaskSingleton.deletedTaskId } ?: return@subscribe
                removedItemIndex = TaskSingleton.openTaskList?.indexOf(removedTask) ?: return@subscribe

                TaskSingleton.openTaskList?.remove(removedTask)
                TaskSingleton.openTaskIdList?.remove(removedItemIndex)

                Log.d("RX_DEBUG", "DELETE TASK: OK")
                Log.d("ROOM_DEBUG", "TASK TO BE DELETED: $task")
                isDeleteTaskSuccess.postValue(true)

            }, { error ->
                Log.e("RX_DEBUG", "DELETE TASK: ${error.message}")
            })

    }

    fun archiveTask(task: Task, context: Context): Disposable? {

        val toDoSharedPref = ToDoSharedPref.getInstance(context) ?: return null

        return Single.create { emitter ->
            emitter.onSuccess(taskDao?.changeIsArchivedById(task.id, true) ?: emitter.onError(NullPointerException()))
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                task.isArchived = true

                if(TaskSingleton.archivedTaskIdList == null)
                    toDoSharedPref.saveArchivedTaskIdList(mutableListOf(task.id))
                else
                    toDoSharedPref.addArchivedTaskIdToList(task.id)

                archivedTask = TaskSingleton.openTaskList?.find { it.isArchived } ?: return@subscribe
                archivedItemIndex = TaskSingleton.openTaskList?.indexOf(archivedTask) ?: return@subscribe

                TaskSingleton.openTaskList?.remove(task)
                toDoSharedPref.removeOpenTaskId(task.id)

                Log.d("RX_DEBUG", "ARCHIVE TASK: OK")
                Log.d("ROOM_DEBUG", "ARCHIVED TASK ID LIST: ${TaskSingleton.archivedTaskIdList}")
                isArchiveTaskSuccess.postValue(true)

            }, { error ->
                Log.e("RX_DEBUG", "ARCHIVE TASK: ${error.message}")
            })

    }

}