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
    fun inserirTask(todo: Task): Completable

    // Atualizar uma tarefa
    @Update
    fun atualizarTask(todo: Task): Completable

    // Deletar uma tarefa
    @Delete
    fun deletarTask(todo: Task): Completable

    // Obter todas as tarefas como LiveData
    @Query("SELECT * FROM task_table ORDER BY id ASC")
    fun getAllTasksLiveData(): LiveData<List<Task>>
}