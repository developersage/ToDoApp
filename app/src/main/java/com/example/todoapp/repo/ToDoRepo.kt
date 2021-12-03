package com.example.todoapp.repo

import android.app.Application
import com.example.todoapp.model.*
import com.example.todoapp.repo.local.ToDoDatabase
import com.example.todoapp.repo.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response

class ToDoRepo(private val app: Application) {

    private val toDoService by lazy { RetrofitInstance.toDoService}
    private val toDoDao by lazy { ToDoDatabase.getToDoDatabase(app).ToDoDao() }

    fun getAllFromDao() = toDoDao.getAllToDos().flowOn(Dispatchers.IO)


    //ToDoService methods
    suspend fun login(userInfo: UserInfo) = toDoService.login(userInfo)

    suspend fun register(userInfo: UserInfo) = toDoService.register(userInfo)

    suspend fun createNew(headers: Map<String, String>, request: ToDoRequest): Response<ToDoBody> {
        val response = toDoService.createNewTodo(headers, request)
        if (response.isSuccessful && response.body() != null) { insertToDo(response.body()!!) }
        return response
    }

    suspend fun editToDo(headers: Map<String, String>, item: ToDoData, request: ToDoRequest
    ): Response<ToDoBody> {
        val response = toDoService.editToDo(headers, item.id, request)
        if (response.isSuccessful && response.body() != null) { updateToDo(response.body()!!) }
        return response
    }

    suspend fun fetchAllToDos(headers: Map<String, String>): Response<List<ToDoBody>> {
        val response = toDoService.getAllToDos(headers)
        if (response.isSuccessful && response.body() != null) {
            eraseAll()
            if (response.body() != emptyList<ToDoBody>()){
                insertAll(response.body()!!)
            }
        }
        return response
    }

    suspend fun deleteToDo(headers: Map<String, String>, item: ToDoData): Response<TokenInfo> {
        val response = toDoService.deleteToDo(headers, item.id)
        if (response.isSuccessful && response.body() != null) { eraseToDo(item) }
        return response
    }

    //    suspend fun fetchToDo(headers: Map<String, String>, id: Int) =
    //        safeResponse(toDoService.getToDo(headers, id), ToDoBody())

    //    //Validate and Store
    //    private fun <T> safeResponse(r: Response<T>, emptyObj: T): T {
    //        return if (r.isSuccessful && r.body() != null) {
    //            r.body()!!
    //        }
    //        else { emptyObj }
    //    }


    //ToDoDao methods
    private suspend fun insertToDo(item: ToDoBody){
        withContext(Dispatchers.IO){
            val todo = ToDoData.convertFromToDoBody(item)
            toDoDao.insertRow(todo)
        }
    }
    private suspend fun insertAll(itemList: List<ToDoBody>) {
        withContext(Dispatchers.IO) {
            toDoDao.insertAll(itemList.map {
                ToDoData.convertFromToDoBody(it)
            })
        }
    }
    private suspend fun updateToDo(item: ToDoBody){
        withContext(Dispatchers.IO){
            toDoDao.updateRow(ToDoData.convertFromToDoBody(item))
        }
    }
    private suspend fun eraseToDo(item: ToDoData){
        withContext(Dispatchers.IO){
            toDoDao.deleteRow(item)
        }
    }
    private suspend fun eraseAll() {
        withContext(Dispatchers.IO) {
            toDoDao.deleteAll()
        }
    }

}