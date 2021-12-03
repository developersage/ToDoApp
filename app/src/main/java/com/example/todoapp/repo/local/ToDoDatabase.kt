package com.example.todoapp.repo.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.model.ToDoData

@Database(entities = [ToDoData::class], version = 1)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun ToDoDao(): ToDoDao

    companion object {
        private const val DATABASE_NAME = "todo_database"

        private lateinit var application: Application

        private val database: ToDoDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(application, ToDoDatabase::class.java, DATABASE_NAME).build()
        }

        fun getToDoDatabase(new_application: Application): ToDoDatabase {
            application = new_application
            return database
        }

    }
}