package com.example.todoapp.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import com.example.todoapp.datastore.TokenStore
import com.example.todoapp.datastore.TokenStoreImp
import com.example.todoapp.model.*
import com.example.todoapp.repo.ToDoRepo
import com.example.todoapp.util.ViewState
import com.example.todoapp.util.display
import com.example.todoapp.util.logMe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ToDoViewModel(app: Application) : AndroidViewModel(app) {

    private val currApp by lazy { app }
    private val tokenPref: TokenStore by lazy { TokenStoreImp(app) }
    private val toDoRepo by lazy { ToDoRepo(app) }

    private val _savedToken = MutableLiveData<TokenInfo?>(null)
    val savedToken: LiveData<TokenInfo?> get() = _savedToken
    var userLoggedIn: Boolean = false

    private val _todos = MutableLiveData<List<ToDoData>>()
    val todos: LiveData<List<ToDoData>> get() = _todos

    init {
        viewModelScope.launch(Dispatchers.Main) {
            userLoggedIn = !userLoggedIn
            tokenPref.getSavedToken().collect {
                userLoggedIn = it.token != ""
                _savedToken.postValue(it)
            }
        }
        viewModelScope.launch(Dispatchers.Default) {
            toDoRepo.getAllFromDao().distinctUntilChanged().collect {
                _todos.postValue(it)
            }
        }
    }

    fun logIn(user: UserInfo) = liveData {
        emit(ViewState.Loading)
        val fetched = toDoRepo.login(user)
        val tokenInfo = fetched.body()

        val state = if (fetched.isSuccessful) {
            tokenPref.setToken(tokenInfo!!)
            ViewState.Success(tokenInfo.message)
        } else{
            "Log In Failed.".let {
                tokenPref.setToken(TokenInfo(it))
                ViewState.Error(it)
            }
        }
        tokenInfo?.message?.display(currApp)
        emit(state)
    }

    fun register(user: UserInfo) = liveData {
        emit(ViewState.Loading)
        val fetched = toDoRepo.register(user)
        val tokenInfo = fetched.body()

        val state = if (fetched.isSuccessful) {
            tokenPref.setToken(tokenInfo!!)
            ViewState.Success(tokenInfo.message)
        } else{
            "Register Failed.".let {
                tokenPref.setToken(TokenInfo(it))
                ViewState.Error(it)
            }
        }
        tokenInfo?.message?.display(currApp)
        emit(state)
    }

    fun logOut() {
        viewModelScope.launch {
            tokenPref.clearToken()
            "Logged Out".display(currApp)
        }
    }

    fun createNew(request: ToDoRequest) = liveData {
        emit(ViewState.Loading)
        tokenPref.getTokenMap().firstOrNull()?.let { tokenMap ->

            val fetched = toDoRepo.createNew(tokenMap, request)

            val state = if (fetched.isSuccessful) {
                "Successfully Composed.".let {
                    it.display(currApp)
                    ViewState.Success(it)
                }
            } else {
                "Compose Failed.".let {
                    it.display(currApp)
                    ViewState.Error(it)
                }
            }
            emit(state)
        }
    }

    fun edit(item: ToDoData, request: ToDoRequest) = liveData {
        emit(ViewState.Loading)
        tokenPref.getTokenMap().firstOrNull()?.let { tokenMap ->

            val fetched = toDoRepo.editToDo(tokenMap, item, request)

            val state = if (fetched.isSuccessful) {
                "Successfully Edited.".let {
                    it.display(currApp)
                    ViewState.Success(it)
                }
            } else {
                "Edit Failed.".let {
                    it.display(currApp)
                    ViewState.Error(it)
                }
            }
            emit(state)
        }
    }

    fun fetchAndLoadAll() = liveData {
        emit(ViewState.Loading)
        tokenPref.getTokenMap().firstOrNull()?.let { token ->

            val fetched = toDoRepo.fetchAllToDos(token)

            val state = if (fetched.isSuccessful){
                "Successfully Synced.".let {
                    it.display(currApp)
                    ViewState.Success(it)
                }
            } else {
                "Sync Failed.".let {
                    it.display(currApp)
                    ViewState.Error(it)
                }
            }
            emit(state)
        }
    }

    fun delete(item: ToDoData) = liveData {
        emit(ViewState.Loading)
        tokenPref.getTokenMap().firstOrNull()?.let { tokenMap ->

            val fetched = toDoRepo.deleteToDo(tokenMap, item)
            val message = fetched.body()?.message

            val state = if (fetched.isSuccessful) { ViewState.Success(message) }
            else { ViewState.Error(message) }
            message?.display(currApp)
            emit(state)
        }
    }


}