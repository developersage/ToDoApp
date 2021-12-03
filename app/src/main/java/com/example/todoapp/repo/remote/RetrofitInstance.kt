package com.example.todoapp.repo.remote

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private const val BASE_URL_TODO_API = "https://knex-todo.herokuapp.com/api/"

    private val toDoRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_TODO_API)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val toDoService: ToDoService = toDoRetrofit.create(ToDoService::class.java)

}