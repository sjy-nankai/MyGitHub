package com.android.github.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object AuthPreferences {
    private val Context.dataStore by preferencesDataStore(name = "auth")

    private val ACCESS_CODE = stringPreferencesKey("access_code")
    private val ACCESS_TOKEN = stringPreferencesKey("access_TOKEN")

    var code = ""

    suspend fun saveAccessToken(context: Context, token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    fun getAccessToken(context: Context): String? =
        runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[ACCESS_TOKEN]
            }.firstOrNull()
        }

    suspend fun saveAccessCode(context: Context, code: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_CODE] = code
        }
    }

    suspend fun removeAccessCode(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_CODE)
        }
    }

    fun getAccessCodeFlow(context: Context) =
            context.dataStore.data.map { preferences ->
                preferences[ACCESS_CODE]
            }

    fun getAccessCode(context: Context): String? =
        runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[ACCESS_CODE]
            }.firstOrNull()
        }


    suspend fun clearAuth(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}