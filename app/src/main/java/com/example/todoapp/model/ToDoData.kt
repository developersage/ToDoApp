package com.example.todoapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.util.convert
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "todolist")
data class ToDoData (
    @PrimaryKey var id: Int,
    @ColumnInfo(name="Title") val title: String,
    @ColumnInfo(name="Description") val description: String?,
    @ColumnInfo(name="Completion Status") val completed: String,
    @ColumnInfo(name="Creation Date and Time") val date: String,
    @ColumnInfo(name="Updated Date and Time") val updateAt: String
): Parcelable {
    companion object{
        fun convertFromToDoBody(todo: ToDoBody) = ToDoData(
            id = todo.id,
            title = todo.title,
            description = todo.description,
            completed = todo.completed.toString(),
            date = todo.date.convert(),
            updateAt = todo.updateAt.convert()
        )
    }
}