package com.helloanwar.vendure.util

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

fun SharedPreferences.Editor.setToken(token: String?) {
    putString("token", token)
    apply()
}

fun SharedPreferences.getToken(): String? {
    return getString("token", null)
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SharedPrefUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val VENDURE_LOGIN_TOKEN = stringPreferencesKey("vendure_token")
    val vendureToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[VENDURE_LOGIN_TOKEN]
        }

    suspend fun setVendureToken(token: String?) {
        context.dataStore.edit { settings ->
            settings[VENDURE_LOGIN_TOKEN] = token ?: ""
        }
    }
}