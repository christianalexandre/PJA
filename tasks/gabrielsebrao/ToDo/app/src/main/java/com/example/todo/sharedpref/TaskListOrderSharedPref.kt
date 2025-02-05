package com.example.todo.sharedpref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private const val KEY_TO_DO = "ToDoSharedPreferences"
private const val KEY_LIST = "PositionListSharedPreferences"

class TaskListOrderSharedPref(context: Context) {

    var list: MutableList<Int>? = null
    private var sharedPref: SharedPreferences? = null
    private var gson: Gson? = null
    private var type: Type? = null

    init {

        sharedPref = context.getSharedPreferences(KEY_TO_DO, Context.MODE_PRIVATE)
        gson = Gson()
        type = object : TypeToken<List<Int>>() {}.type
        list = getIdList()

    }

    fun saveList(list: MutableList<Int>?) {

        sharedPref?.edit()?.apply {
            putString(KEY_LIST, gson?.toJson(list))
        }?.apply()

        this.list = list

    }

    fun addId(id: Int) {

        list?.add(0, id)

        saveList(list)

    }

    private fun getIdList(): MutableList<Int>? = gson?.fromJson(sharedPref?.getString(KEY_LIST, null), type)

}