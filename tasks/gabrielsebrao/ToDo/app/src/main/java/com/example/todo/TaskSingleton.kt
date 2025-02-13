package com.example.todo

import com.example.todo.room.Task

object TaskSingleton {

    var openTaskList: MutableList<Task>? = null
    var archivedTaskList: MutableList<Task>? = null

    var openTaskIdList: MutableList<Int>? = null
    var archivedTaskIdList: MutableList<Int>? = null

    var newTask: Task? = null
    var deletedTaskId: Int? = null

}