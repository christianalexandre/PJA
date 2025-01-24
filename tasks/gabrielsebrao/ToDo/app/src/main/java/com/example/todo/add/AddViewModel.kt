package com.example.todo.add

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.todo.room.Task
import com.example.todo.room.TaskDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddViewModel: ViewModel() {

    fun addTask(taskDao: TaskDao, title: String, content: String): Disposable {
        return Disposable.empty()
    }

}