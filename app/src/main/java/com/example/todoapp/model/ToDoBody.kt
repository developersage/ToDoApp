package com.example.todoapp.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ToDoBody(
    val id: Int = -1,
    val title: String = "",
    val description: String? = "",
    val completed: Boolean = false,
    val userId: Int = -1,
    val date: String = "",
    val updateAt: String = ""
): Parcelable

@JsonClass(generateAdapter = true)
data class UserInfo(
    val username: String,
    val password: String,
    val email: String = ""
)

@JsonClass(generateAdapter = true)
data class TokenInfo(
    val message: String = "",
    val token: String = ""
)

@JsonClass(generateAdapter = true)
data class Message(
    val message: String = ""
)

@JsonClass(generateAdapter = true)
data class ToDoRequest(
    val title: String,
    val description: String? = null,
    val completed: Boolean = false
)