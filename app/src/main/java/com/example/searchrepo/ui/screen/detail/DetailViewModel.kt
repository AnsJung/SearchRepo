package com.example.searchrepo.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.searchrepo.data.local.PreferenceRepository
import com.example.searchrepo.ui.model.toFavoritePreference
import com.example.searchrepo.ui.navigation.Route
import com.example.searchrepo.ui.navigation.util.createNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailRepoModel = savedStateHandle.toRoute<Route.Detail>(
        typeMap = mapOf(
            typeOf<DetailRepoModel>() to createNavType<DetailRepoModel>()
        )
    ).detailRepoModel
    val detailRepoModel: DetailRepoModel = _detailRepoModel
    val isFavorite: StateFlow<Boolean> = preferenceRepository.favoriteRepos
        .map { list ->
            // 현재 리스트에 내 아이디가 있는지 확인
            list.any { it.id == _detailRepoModel.id }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun toggleFavoriteItem() {
        viewModelScope.launch {
            preferenceRepository.toggleFavorite(detailRepoModel.toUiModel().toFavoritePreference())
        }
    }
}
