package com.example.todo.home

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todo.TaskSingleton
import com.example.todo.room.DataBase
import com.example.todo.room.Task
import com.example.todo.room.TaskDao
import com.example.todo.sharedpref.ToDoSharedPref
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.ref.WeakReference

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val contextRef: WeakReference<Context> = WeakReference(application.applicationContext)
    private val sharedPref: ToDoSharedPref? = ToDoSharedPref.getInstance(contextRef.get())
    private val taskDao: TaskDao? = DataBase.getInstance(contextRef.get())?.taskDao()

    val isDeleteTaskSuccess: MutableLiveData<Boolean> = MutableLiveData(false)

    private var removedTask: Task? = null
    var removedItemIndex: Int = 0

    fun deleteTask(task: Task?): Disposable? {

        if(task == null)
            return null

        return Single.create { emitter ->
            emitter.onSuccess(taskDao?.deleteTaskById(task.id) ?: emitter.onError(NullPointerException()))
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                removedTask = TaskSingleton.openTaskList?.find { it.id == task.id } ?: return@subscribe
                removedItemIndex = TaskSingleton.openTaskList?.indexOf(removedTask) ?: return@subscribe

                TaskSingleton.openTaskList?.remove(removedTask)
                sharedPref?.removeOpenTaskId(task.id)

                Log.d("RX_DEBUG", "DELETE TASK: OK")
                Log.d("ROOM_DEBUG", "TASK TO BE DELETED: $task")
                isDeleteTaskSuccess.postValue(true)

            }, { error ->
                Log.e("RX_DEBUG", "DELETE TASK: ${error.message}")
            })

    }

}