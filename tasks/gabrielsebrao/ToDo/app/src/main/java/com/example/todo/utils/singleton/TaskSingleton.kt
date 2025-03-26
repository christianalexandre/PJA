package com.example.todo.utils.singleton

import com.example.todo.utils.models.Task

object TaskSingleton {

    var openTaskList: MutableList<Task>? = null
    var archivedTaskList: MutableList<Task>? = null

    var openTaskIdList: MutableList<Int>? = null
    var archivedTaskIdList: MutableList<Int>? = null

}