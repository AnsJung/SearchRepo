package com.example.searchrepo.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.searchrepo.ui.model.RepoUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore("configs")

class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode_enabled")
    private val FAVORITE_LIST_KEY = stringPreferencesKey("favorite_list")

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: false
    }

    val favoriteRepos: Flow<List<RepoUiModel>> = context.dataStore.data
        .map { preferences ->
            val json = preferences[FAVORITE_LIST_KEY] ?: "[]"
            Json.decodeFromString<List<RepoUiModel>>(json)
        }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    suspend fun toggleFavorite(repo: RepoUiModel) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[FAVORITE_LIST_KEY] ?: "[]"
            val currentList = Json.decodeFromString<List<RepoUiModel>>(currentJson)

            val isExist = currentList.any { it.id == repo.id }
            val newList = if (isExist) {
                currentList.filter { it.id != repo.id }
            } else {
                currentList + repo
            }

            preferences[FAVORITE_LIST_KEY] = Json.encodeToString(newList)
        }
    }

    suspend fun isFavorite(repoId: Int): Boolean {
        val preferences = context.dataStore.data.first() // 현재 상태를 한 번 읽어옴
        val json = preferences[FAVORITE_LIST_KEY] ?: "[]"
        val favoriteList = Json.decodeFromString<List<RepoUiModel>>(json)

        return favoriteList.any { it.id == repoId }
    }

}