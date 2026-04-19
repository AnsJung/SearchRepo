package com.example.searchrepo.data.local

import com.example.searchrepo.ui.model.RepoOriginModel
import kotlinx.coroutines.flow.MutableStateFlow

class FakePreferenceRepository : PreferenceRepository {

    override val isDarkMode = MutableStateFlow(false)
    override val favoriteRepos = MutableStateFlow<List<RepoOriginModel>>(emptyList())

    override suspend fun setDarkMode(enabled: Boolean) {
        isDarkMode.value = enabled
    }

    override suspend fun toggleFavorite(repo: RepoOriginModel) {
        val currentList = favoriteRepos.value
        favoriteRepos.value =
            if (currentList.any { it.id == repo.id }) {
                currentList.filter { it.id != repo.id }
            } else {
                currentList + repo
            }
    }
}