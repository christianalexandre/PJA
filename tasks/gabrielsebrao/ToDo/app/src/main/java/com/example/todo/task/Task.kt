package com.example.todo.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "isArchived") var isArchived: Boolean
) {
    init {
        if(title.length > 50) throw(IllegalArgumentException("TITLE length cannot exceed 50 characters"))
        if(content.length > 300) throw(IllegalArgumentException("CONTENT length cannot exceed 300 characters"))
    }
}