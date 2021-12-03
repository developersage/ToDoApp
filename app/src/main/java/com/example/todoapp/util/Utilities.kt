package com.example.todoapp.util

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.todoapp.model.ToDoBody
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

val ViewGroup.layoutInflater: LayoutInflater get() = LayoutInflater.from(context)

fun String.logMe(tag: String = "ToDoLogTag") {
    Log.d(tag, this)
}

fun String.convert(): String {
    return if (this != ""){
        SimpleDateFormat("yyyy-MM-dd, HH:mm:ss ", Locale.US)
            .format(Date(this.toLong() * 1000))
    } else {
        "yyyy-MM-dd, HH:mm:ss "
    }
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) &&
            android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.display(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}
