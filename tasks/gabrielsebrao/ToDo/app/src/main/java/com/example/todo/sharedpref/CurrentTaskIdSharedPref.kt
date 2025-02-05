package com.example.todo.sharedpref

import android.content.Context
import android.content.SharedPreferences

private const val KEY_TO_DO = "ToDoSharedPreferences"
private const val KEY_CURRENT_TASK_ID = "CurrentTaskIdSharedPreferences"
private const val DEFAULT_NEXT_TASK_ID = 1

class CurrentTaskIdSharedPref(context: Context) {

    var nextTaskId: Int = DEFAULT_NEXT_TASK_ID
    private var sharedPref: SharedPreferences? = null

    init {

        sharedPref = context.getSharedPreferences(KEY_TO_DO, Context.MODE_PRIVATE)
        nextTaskId = getCurrentTaskId()

    }

    fun incrementCurrentTaskId() {

        nextTaskId += 1

        sharedPref?.edit()?.apply {
            putInt(KEY_CURRENT_TASK_ID, nextTaskId)
        }?.apply()

    }

    private fun getCurrentTaskId(): Int = sharedPref?.getInt(KEY_CURRENT_TASK_ID, DEFAULT_NEXT_TASK_ID) ?: DEFAULT_NEXT_TASK_ID

}