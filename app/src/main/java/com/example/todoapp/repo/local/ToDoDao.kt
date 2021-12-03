package com.example.todoapp.repo.local

import androidx.room.*
import com.example.todoapp.model.ToDoData
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todolist")
    fun getAllToDos(): Flow<List<ToDoData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRow(todo: ToDoData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(todos: List<ToDoData>)

    @Update
    fun updateRow(todo: ToDoData)

    @Delete
    fun deleteRow(todo: ToDoData)

    @Query("DELETE FROM todolist")
    fun deleteAll()

}