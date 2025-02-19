package com.example.todo.archived

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

class ArchivedViewModel(application: Application): AndroidViewModel(application) {

    private val contextRef: WeakReference<Context> = WeakReference(application.applicationContext)
    private val sharedPref: ToDoSharedPref? = ToDoSharedPref.getInstance(contextRef.get())
    private val taskDao: TaskDao? = DataBase.getInstance(contextRef.get())?.taskDao()

    private val _deleteTaskState: MutableLiveData<TaskState?> = MutableLiveData()

    val deleteTaskState: LiveData<TaskState?> = _deleteTaskState

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

                removedTask = TaskSingleton.archivedTaskList?.find { it.id == task.id } ?: return@subscribe
                removedItemIndex = TaskSingleton.archivedTaskList?.indexOf(removedTask) ?: return@subscribe

                TaskSingleton.archivedTaskList?.remove(removedTask)
                sharedPref?.removeArchivedTaskId(task.id)

                Log.d("RX_DEBUG", "DELETE TASK: OK")
                Log.d("ROOM_DEBUG", "TASK TO BE DELETED: $task")
                _deleteTaskState.postValue(TaskState.Success)

            }, { error ->
                _deleteTaskState.postValue(TaskState.Error)
                Log.e("RX_DEBUG", "DELETE TASK: ${error.message}")
            })

    }

}