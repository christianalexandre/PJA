package com.example.todo.utils.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.utils.models.Task

@Database(entities = [Task::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {

        @Volatile
        private var INSTANCE: DataBase? = null

        fun getInstance(context: Context?): DataBase? {

            if(context == null)
                return null

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "ToDoDataBase"
                ).build()

                INSTANCE = instance

                instance
            }
        }

    }
}