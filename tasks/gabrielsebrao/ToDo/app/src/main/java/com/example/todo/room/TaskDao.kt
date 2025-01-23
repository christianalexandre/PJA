package com.example.todo.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE id IN (:taskIds)")
    fun findByIds(taskIds: IntArray): List<Task>

    @Query("SELECT * FROM task WHERE title LIKE :title")
    fun findByTitle(title: String): Task

    @Insert
    fun insertAll(vararg tasks: Task)

    @Delete
    fun delete(task: Task)
}