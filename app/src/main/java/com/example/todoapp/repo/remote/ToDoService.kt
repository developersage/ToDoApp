package com.example.todoapp.repo.remote

import com.example.todoapp.model.*
import retrofit2.Response
import retrofit2.http.*

interface ToDoService {

    @POST("auth/register")
    suspend fun register(
        @Body regInfo: UserInfo
    ): Response<TokenInfo>

    @POST("auth/login")
    suspend fun login(
        @Body logInfo: UserInfo
    ): Response<TokenInfo>

    @POST("todos")
    suspend fun createNewTodo(
        @HeaderMap header: Map<String, String>,
        @Body toDoReq: ToDoRequest
    ): Response<ToDoBody>

    @GET("todos")
    suspend fun getAllToDos(
        @HeaderMap header: Map<String, String>,
    ): Response<List<ToDoBody>>

    @GET("todos/{id}")
    suspend fun getToDo(
        @HeaderMap header: Map<String, String>,
        @Path("id") id: Int
    ): Response<ToDoBody>

    @DELETE("todos/{id}")
    suspend fun deleteToDo(
        @HeaderMap header: Map<String, String>,
        @Path("id") id: Int
    ): Response<TokenInfo>

    @PUT("todos/{id}")
    suspend fun editToDo(
        @HeaderMap header: Map<String, String>,
        @Path("id") id: Int,
        @Body toDoReq: ToDoRequest
    ): Response<ToDoBody>

}