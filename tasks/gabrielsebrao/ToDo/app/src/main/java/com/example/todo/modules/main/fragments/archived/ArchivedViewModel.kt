package com.example.todo.modules.main.fragments.archived

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

class ArchivedViewModel(application: Application): AndroidViewModel(application) {

    private val contextRef: WeakReference<Context> = WeakReference(application.applicationContext)
    private val sharedPref: TaskIdListSharedPref? = TaskIdListSharedPref.getInstance(contextRef.get())
    private val taskDao: TaskDao? = DataBase.getInstance(contextRef.get())?.taskDao()

    private val _deleteTaskSuccess: MutableLiveData<Boolean?> = MutableLiveData()

    val deleteTaskSuccess: LiveData<Boolean?> = _deleteTaskSuccess

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
                _deleteTaskSuccess.postValue(true)

            }, { error ->
                _deleteTaskSuccess.postValue(false)
                Log.e("RX_DEBUG", "DELETE TASK: ${error.message}")
            })

    }

}