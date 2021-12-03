package com.example.todoapp.datastore

import com.example.todoapp.model.TokenInfo
import kotlinx.coroutines.flow.Flow

interface TokenStore {

    fun getSavedToken(): Flow<TokenInfo>
    fun getTokenMap(): Flow<Map<String, String>>

    suspend fun setToken(tokenInfo: TokenInfo)
    suspend fun clearToken()

}