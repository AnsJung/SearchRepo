package com.example.searchrepo.ui.screen.favorite

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchrepo.data.local.PreferenceManager
import com.example.searchrepo.ui.model.RepoUiModel
import com.example.searchrepo.ui.model.toDetailModel
import com.example.searchrepo.ui.model.toFavoriteModel
import com.example.searchrepo.ui.model.toMainModel
import com.example.searchrepo.ui.screen.detail.DetailRepoModel
import com.example.searchrepo.ui.screen.main.MainRepoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private var _originRepos = MutableStateFlow<List<RepoUiModel>>(emptyList())
    val originRepos: StateFlow<List<RepoUiModel>> = _originRepos.asStateFlow()
    val favoriteRepos: StateFlow<List<FavoriteRepoModel>> = preferenceManager.favoriteRepos
        .onEach { newList ->
            _originRepos.value = newList
        }
        .map { list ->
            list.map { it.toFavoriteModel() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFavorite(id: Int) {
        viewModelScope.launch {
            // 원본 리스트에서 해당 ID의 객체를 찾아 삭제 요청
            val target = _originRepos.value.find { it.id == id }
            target?.let {
                preferenceManager.toggleFavorite(it)
            }
        }
    }

    fun getDetailModel(id: Int): DetailRepoModel? {
        val rawRepo = _originRepos.value.find { it.id == id }
        return rawRepo?.toDetailModel()?.copy(isFavorite = true)
    }

}