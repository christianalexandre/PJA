package com.example.todolist.ui.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.ui.database.dao.TaskDao
import com.example.todolist.ui.database.model.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}