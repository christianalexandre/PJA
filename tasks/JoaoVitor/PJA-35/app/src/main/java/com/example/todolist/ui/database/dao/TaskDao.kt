package com.example.todolist.ui.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.ui.database.model.Task
import io.reactivex.rxjava3.core.Completable

@Dao
interface TaskDao {
    // Inserir uma tarefa
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(todo: Task): Completable

    // Atualizar uma tarefa
    @Update
    fun atualizarTask(todo: Task): Completable

    // Deletar uma tarefa
    @Delete
    fun deletarTask(todo: Task): Completable

    // Obter todas as tarefas como LiveData
    @Query("SELECT * FROM task_table ORDER BY id ASC")
    fun getAllTasksLiveData(): LiveData<List<Task>>

    // Obtém apenas as tarefas ativas (não arquivadas)
    @Query("SELECT * FROM task_table WHERE isArchived = 0 ORDER BY id ASC")
    fun getActiveTasksLiveData(): LiveData<List<Task>>

    // Obtém apenas as tarefas arquivadas
    @Query("SELECT * FROM task_table WHERE isArchived = 1 ORDER BY id ASC")
    fun getArchivedTasksLiveData(): LiveData<List<Task>>
}