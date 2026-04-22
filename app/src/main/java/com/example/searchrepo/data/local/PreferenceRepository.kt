package com.example.searchrepo.data.local

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    val isDarkMode: Flow<Boolean>
    val favoriteRepos: Flow<List<FavoriteRepoPreference>>

    suspend fun setDarkMode(enabled: Boolean)
    suspend fun toggleFavorite(repo: FavoriteRepoPreference)
}
