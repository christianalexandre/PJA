package com.example.todo.sharedpref

import android.content.Context
import com.example.todo.TaskSingleton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private const val KEY_TO_DO = "ToDoSharedPreferences"
private const val KEY_CURRENT_TASK_ID = "CurrentTaskIdSharedPreferences"
private const val DEFAULT_NEXT_TASK_ID = 1
private const val KEY_LIST = "PositionListSharedPreferences"
private const val ARCHIVED_KEY_LIST = "ArchivedPositionListSharedPreferences"

class ToDoSharedPref private constructor(context: Context) {

    private val appContext: Context = context.applicationContext
    private val gson = Gson()
    private val sharedPref = appContext.getSharedPreferences(KEY_TO_DO, Context.MODE_PRIVATE)
    private var type: Type? = null
    var nextTaskId = getCurrentTaskIdFromSharedPref()

    companion object {

        @Volatile
        private var INSTANCE: ToDoSharedPref? = null

        fun getInstance(context: Context?): ToDoSharedPref? {

            if(context == null)
                return null

            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ToDoSharedPref(context).also { INSTANCE = it }
            }
        }

    }

    init {
        TaskSingleton.openTaskIdList = getOpenTaskIdListFromSharedPref()
        TaskSingleton.archivedTaskIdList = getArchivedTaskIdListFromSharedPref()
    }

    fun incrementCurrentTaskId() {

        nextTaskId += 1

        sharedPref?.edit()?.apply {
            putInt(KEY_CURRENT_TASK_ID, nextTaskId)
        }?.apply()

    }

    private fun getCurrentTaskIdFromSharedPref(): Int =
        sharedPref?.getInt(KEY_CURRENT_TASK_ID, DEFAULT_NEXT_TASK_ID) ?: DEFAULT_NEXT_TASK_ID

    private fun getOpenTaskIdListFromSharedPref(): MutableList<Int>? {
        type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(sharedPref?.getString(KEY_LIST, null), type)
    }

    private fun getArchivedTaskIdListFromSharedPref(): MutableList<Int>? {
        type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(sharedPref?.getString(ARCHIVED_KEY_LIST, null), type)
    }

    fun saveOpenTaskIdList(list: MutableList<Int>?) {

        sharedPref?.edit()?.apply {
            putString(KEY_LIST, gson.toJson(list))
        }?.apply()

        TaskSingleton.openTaskIdList = list

    }

    fun saveArchivedTaskIdList(list: MutableList<Int>?) {

        sharedPref?.edit()?.apply {
            putString(ARCHIVED_KEY_LIST, gson.toJson(list))
        }?.apply()

        TaskSingleton.archivedTaskIdList = list

    }

    fun addOpenTaskIdToList(id: Int) {
        TaskSingleton.openTaskIdList?.add(0, id)
        saveOpenTaskIdList(TaskSingleton.openTaskIdList)
    }

    fun addArchivedTaskIdToList(id: Int) {
        TaskSingleton.archivedTaskIdList?.add(0, id)
        saveArchivedTaskIdList(TaskSingleton.archivedTaskIdList)
    }

    fun removeOpenTaskId(id: Int) {
        TaskSingleton.openTaskIdList?.remove(id)
        saveOpenTaskIdList(TaskSingleton.openTaskIdList)
    }

    fun removeArchivedTaskId(id: Int) {
        TaskSingleton.archivedTaskIdList?.remove(id)
        saveArchivedTaskIdList(TaskSingleton.archivedTaskIdList)
    }

}