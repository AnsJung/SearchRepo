package com.example.searchrepo.ui.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.searchrepo.data.repository.GithubRepository
import com.example.searchrepo.ui.common.ApiResult
import com.example.searchrepo.ui.model.RepoUiModel
import com.example.searchrepo.ui.model.toDetailModel
import com.example.searchrepo.ui.model.toMainModel
import com.example.searchrepo.ui.screen.detail.DetailRepoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {

    private var originResponse: List<RepoUiModel> = emptyList()
    private val _uiState = MutableStateFlow(RepoUiState())
    val uiState: StateFlow<RepoUiState> = _uiState.asStateFlow()
    private var lastSearchQuery: String = ""

    init {
        viewModelScope.launch {
            _uiState
                .map { it.searchText }
                .distinctUntilChanged()
                .onEach { query ->
                    // [추가] 검색어가 비어있을 경우 리스트를 즉시 비움
                    if (query.isBlank()) {
                        _uiState.update {
                            it.copy(repos = emptyList(), hasSearched = false)
                        }
                    }
                }
                .debounce(500)
                .filter { it.isNotBlank() }
                .flatMapLatest { query ->
                    flow {
                        emit(requestRepoList(query))
                    }
                }
                .collect()
        }
    }

    fun onSearchTextChange(newSearchText: String) {
        _uiState.update { it.copy(searchText = newSearchText) }
    }

    fun requestRepoList(
        query: String = _uiState.value.searchText
    ) {
        if (query.isBlank() || (query == lastSearchQuery && _uiState.value.repos.isNotEmpty())) {
            Log.e("JH","중복 방지")
            return
        }
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            lastSearchQuery = query
            _uiState.update { it.copy(isLoading = true, error = null, hasSearched = true) }
            when (val result = githubRepository.requestRepoList(query)) {
                is ApiResult.Success -> {
                    originResponse = result.data
                    val mainData = originResponse.map { it.toMainModel() }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            repos = mainData
                        )
                    }
                }

                is ApiResult.Error -> {
                    Log.e("JH", "에러")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }

                else -> {}
            }
        }
    }

    fun getDetailItem(id: Int): DetailRepoModel? {
        Log.e("JH", "id >> $id")
        return originResponse.find { it.id == id }
            ?.toDetailModel()
    }

    fun refreshSearched() {
        if (_uiState.value.isLoading) return
        if (_uiState.value.searchText.isBlank() && _uiState.value.repos.isEmpty()) {
            _uiState.update {
                it.copy(
                    showGuideDialog = true
                )
            }
            return
        }
        _uiState.update {
            it.copy(
                searchText = "",
                repos = emptyList(),
                hasSearched = false
            )
        }
    }

    fun onDialogDismiss() {
        _uiState.update {
            it.copy(
                showGuideDialog = false
            )
        }
    }
}

data class RepoUiState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
    val repos: List<MainRepoModel> = emptyList(),
    val error: String? = null,
    val showGuideDialog: Boolean = false
)