package com.example.todo.modules.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todo.utils.singleton.TaskSingleton
import com.example.todo.utils.database.DataBase
import com.example.todo.utils.models.Task
import com.example.todo.utils.database.TaskDao
import com.example.todo.utils.sharedpref.TaskIdListSharedPref
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.ref.WeakReference

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val contextRef: WeakReference<Context> = WeakReference(application.applicationContext)
    private val sharedPref: TaskIdListSharedPref? = TaskIdListSharedPref.getInstance(contextRef.get())
    private val taskDao: TaskDao? = DataBase.getInstance(contextRef.get())?.taskDao()

    private val _addTaskSuccess: MutableLiveData<Boolean?> = MutableLiveData()
    private val _archiveTaskSuccess: MutableLiveData<Boolean?> = MutableLiveData()
    private val _unarchiveTaskSuccess: MutableLiveData<Boolean?> = MutableLiveData()

    val addTaskSuccess: LiveData<Boolean?> = _addTaskSuccess
    val archiveTaskSuccess: LiveData<Boolean?> = _archiveTaskSuccess
    val unarchiveTaskSuccess: LiveData<Boolean?> = _unarchiveTaskSuccess

    private var archivedTask: Task? = null
    private var unarchivedTask: Task? = null

    var archivedItemIndex: Int = 0
    var unarchivedItemIndex: Int = 0

    fun addTask(title: String, content: String, byteArrayBitmap: ByteArray?, conclusionDate: Long?): Disposable? {

        val task: Task?

        try {
            task = Task(
                sharedPref?.nextTaskId ?: 0,
                title,
                content,
                false,
                byteArrayBitmap,
                conclusionDate
            )
        } catch(error: Exception) {
            _addTaskSuccess.value = false
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
                Log.d("RX_DEBUG", task.toString())

                sharedPref?.incrementCurrentTaskId()
                TaskSingleton.openTaskList?.add(0, task)

                _addTaskSuccess.postValue(true)

            }, { error ->
                _addTaskSuccess.postValue(false)
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

                Log.d("RX_DEBUG", "ARCHIVE TASK: OK")
                _archiveTaskSuccess.postValue(true)

            }, { error ->
                _archiveTaskSuccess.postValue(false)
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

                Log.e("RX_DEBUG", "UNARCHIVE TASK: OK")
                _unarchiveTaskSuccess.postValue(true)

            }, { error ->
                _unarchiveTaskSuccess.postValue(false)
                Log.e("RX_DEBUG", "UNARCHIVE TASK: ${error.message}")
            })
    }

}