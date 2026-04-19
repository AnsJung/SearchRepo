package com.example.searchrepo.data.local

import com.example.searchrepo.ui.model.RepoOriginModel
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    val isDarkMode: Flow<Boolean>
    val favoriteRepos: Flow<List<RepoOriginModel>>

    suspend fun setDarkMode(enabled: Boolean)
    suspend fun toggleFavorite(repo: RepoOriginModel)
}