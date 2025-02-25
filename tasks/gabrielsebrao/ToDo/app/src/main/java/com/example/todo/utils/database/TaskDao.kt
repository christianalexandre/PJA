package com.example.todo.utils.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todo.utils.models.Task
import io.reactivex.rxjava3.core.Completable

@Dao
interface TaskDao {
    @Query("SELECT * FROM task ORDER BY id DESC")
    fun getAll(): List<Task>

    @Query("DELETE FROM task WHERE id = :id")
    fun deleteTaskById(id: Int)

    @Insert
    fun insertAll(vararg tasks: Task): Completable

    @Query("UPDATE task SET isArchived = :isArchived WHERE id = :id")
    fun changeIsArchivedById(id: Int, isArchived: Boolean)

}