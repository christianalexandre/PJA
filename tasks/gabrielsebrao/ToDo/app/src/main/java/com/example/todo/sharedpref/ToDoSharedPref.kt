package com.example.todo.sharedpref

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private const val KEY_TO_DO = "ToDoSharedPreferences"
private const val KEY_CURRENT_TASK_ID = "CurrentTaskIdSharedPreferences"
private const val DEFAULT_NEXT_TASK_ID = 1
private const val KEY_LIST = "PositionListSharedPreferences"

class ToDoSharedPref private constructor(context: Context) {

    private val appContext: Context = context.applicationContext
    private val gson = Gson()
    private val sharedPref = appContext.getSharedPreferences(KEY_TO_DO, Context.MODE_PRIVATE)
    private var type: Type? = null
    var nextTaskId = getCurrentTaskIdFromSharedPref()
    var idList = getIdListFromSharedPref()


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
        nextTaskId = getCurrentTaskIdFromSharedPref()
        idList = getIdListFromSharedPref()
    }

    fun incrementCurrentTaskId() {

        nextTaskId += 1

        sharedPref?.edit()?.apply {
            putInt(KEY_CURRENT_TASK_ID, nextTaskId)
        }?.apply()

    }

    private fun getCurrentTaskIdFromSharedPref(): Int {
        type = object : TypeToken<List<Int>>() {}.type
        return sharedPref?.getInt(KEY_CURRENT_TASK_ID, DEFAULT_NEXT_TASK_ID) ?: DEFAULT_NEXT_TASK_ID
    }

    fun saveList(list: MutableList<Int>?) {

        sharedPref?.edit()?.apply {
            putString(KEY_LIST, gson.toJson(list))
        }?.apply()

        this.idList = list

    }

    fun addIdToList(id: Int) {
        idList?.add(0, id)
        saveList(idList)
    }

    private fun getIdListFromSharedPref(): MutableList<Int>? {
        type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(sharedPref?.getString(KEY_LIST, null), type)
    }
}