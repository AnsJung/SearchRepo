package com.example.searchrepo.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.searchrepo.data.local.PreferenceManager
import com.example.searchrepo.ui.navigation.Route
import com.example.searchrepo.ui.navigation.util.createNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailRepoModel = savedStateHandle.toRoute<Route.Detail>(
        typeMap = mapOf(
            typeOf<DetailRepoModel>() to createNavType<DetailRepoModel>()
        )
    ).detailRepoModel
    val detailRepoModel: DetailRepoModel = _detailRepoModel
    private val _isFavorite = MutableStateFlow(detailRepoModel.isFavorite)
    val isFavorite = _isFavorite.asStateFlow()

    fun toggleFavoriteItem() {
        viewModelScope.launch {
            preferenceManager.toggleFavorite(detailRepoModel.toUiModel())
            _isFavorite.value = !_isFavorite.value
        }
    }
}