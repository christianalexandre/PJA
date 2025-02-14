package com.example.todolist.ui.database.instance

import android.content.Context
import androidx.room.Room
import com.example.todolist.ui.database.db.AppDatabase

object DatabaseInstance {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "todo_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
