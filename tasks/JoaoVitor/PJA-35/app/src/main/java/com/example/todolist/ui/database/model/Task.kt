package com.example.todolist.ui.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String? = null,
    val image: String? = null,
    val date: String? = null,
    var isArchived: Boolean = false,
    var isSelected: Boolean = false
)
