package com.example.todo.modules.splash

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todo.utils.database.DataBase
import com.example.todo.utils.database.TaskDao
import com.example.todo.utils.sharedpref.TaskIdListSharedPref
import com.example.todo.utils.singleton.TaskSingleton
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.ref.WeakReference

class SplashViewModel(application: Application): AndroidViewModel(application) {

    private val contextRef: WeakReference<Context> = WeakReference(application.applicationContext)
    private val taskDao: TaskDao? = DataBase.getInstance(contextRef.get())?.taskDao()

    private val _getTasksSuccess: MutableLiveData<Boolean?> = MutableLiveData()
    val getTasksSuccess: LiveData<Boolean?> = _getTasksSuccess

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

                Log.d("RX_DEBUG", "GET ALL TASK: OK")
                _getTasksSuccess.postValue(true)

            }, { error ->
                _getTasksSuccess.postValue(false)
                Log.e("RX_DEBUG", "GET ALL TASKS: ${error.message}")
            })

    }

}