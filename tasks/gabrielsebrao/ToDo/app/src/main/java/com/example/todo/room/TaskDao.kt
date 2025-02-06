package com.example.todo.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable

@Dao
interface TaskDao {
    @Query("SELECT * FROM task ORDER BY id DESC")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE id IN (:taskIds)")
    fun findByIds(taskIds: IntArray): List<Task>

    @Query("SELECT * FROM task WHERE title LIKE :title")
    fun findByTitle(title: String): Task

    @Insert
    fun insertAll(vararg tasks: Task): Completable

    @Delete
    fun delete(task: Task): Completable

    @Query("UPDATE task SET title = :title WHERE id = :id")
    fun changeTitleById(id: Int, title: String): Completable

    @Query("UPDATE task SET content = :content WHERE id = :id")
    fun changeContentById(id: Int, content: String): Completable

    @Query("UPDATE task SET isArchived = :isArchived WHERE id = :id")
    fun changeIsArchivedById(id: Int, isArchived: Boolean)

}