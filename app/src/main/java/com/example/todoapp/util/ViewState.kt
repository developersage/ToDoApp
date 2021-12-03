package com.example.todoapp.util

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Success<T>(val item: T) : ViewState<T>()
    data class Error(val errorMsg: String?) : ViewState<Nothing>()
}
