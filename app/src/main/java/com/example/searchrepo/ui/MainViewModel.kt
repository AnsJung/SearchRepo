package com.example.searchrepo.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchrepo.data.repository.GithubRepository
import com.example.searchrepo.ui.common.ApiResult
import com.example.searchrepo.ui.model.RepoUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {
    // 내부에서만 수정 가능한 MutableStateFlow
    private val _uiState = MutableStateFlow(RepoUiState())

    // Compose에서 관찰할 수 있는 읽기 전용 StateFlow
    val uiState: StateFlow<RepoUiState> = _uiState.asStateFlow()

    fun onSearchTextChanged(newSearchText: String) {
        _uiState.update { it.copy(searchText = newSearchText) }
    }

    fun requestRepoList() {
        val query = _uiState.value.searchText
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, hasSearched = true) }
            when (val result = githubRepository.requestRepoList(query)) {
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            repos = result.data
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
}

data class RepoUiState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
    val repos: List<RepoUiModel> = emptyList(),
    val error: String? = null
)