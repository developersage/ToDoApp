package com.example.todoapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.todoapp.model.TokenInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_data_store")

class TokenStoreImp(context: Context): TokenStore {

    private val dataStore by lazy { context.dataStore }

    override fun getSavedToken(): Flow<TokenInfo> = dataStore.data.catch { it
        if (it is IOException) { emit(emptyPreferences()) }
        else{ throw it }
    }.map {
        val message = it[MESSAGE] ?: ""
        val token = it[SAVED_TOKEN] ?: ""
        TokenInfo(message, token)
    }

    override fun getTokenMap(): Flow<Map<String, String>> = dataStore.data.catch { it
        if (it is IOException) { emit(emptyPreferences()) }
        else{ throw it }
    }.map {
        mapOf("Authorization" to (it[SAVED_TOKEN] ?: ""))
    }

    override suspend fun setToken(tokenInfo: TokenInfo) {
        dataStore.edit {
            it[SAVED_TOKEN] = tokenInfo.token
            it[MESSAGE] = tokenInfo.message
        }
    }

    override suspend fun clearToken() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object{
        val SAVED_TOKEN = stringPreferencesKey("saved_token")
        val MESSAGE = stringPreferencesKey("message")
    }

}

////DataStore methods
//private object PreferencesKeys {
//    val SAVED_TOKEN = stringPreferencesKey("saved_token")
//    val MESSAGE = stringPreferencesKey("message")
//}
//
//fun getSavedToken() = dataStore.data.catch{ it
//    if (it is IOException){
//        emit(emptyPreferences())
//    }else{
//        throw it
//    }}.map{
//    val message = it[PreferencesKeys.MESSAGE] ?: ""
//    val token = it[PreferencesKeys.SAVED_TOKEN] ?: ""
//    TokenInfo(message, token)
//}
//
//suspend fun setSavedToken(tokenInfo: TokenInfo) {
//    dataStore.edit {
//        it[PreferencesKeys.SAVED_TOKEN] = tokenInfo.token
//        it[PreferencesKeys.MESSAGE] = tokenInfo.message
//    }
//    Toast.makeText(current_app, tokenInfo.message, Toast.LENGTH_SHORT).show()
//}
//
//suspend fun clearToken() {
//    dataStore.edit {
//        it.clear()
//    }
//}