package com.example.searchrepo.ui.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchrepo.data.repository.GithubRepository
import com.example.searchrepo.ui.common.ApiResult
import com.example.searchrepo.ui.model.RepoUiModel
import com.example.searchrepo.ui.model.toDetailModel
import com.example.searchrepo.ui.model.toMainModel
import com.example.searchrepo.ui.screen.detail.DetailRepoModel
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

    private var originResponse: List<RepoUiModel> = emptyList()
    private val _uiState = MutableStateFlow(RepoUiState())
    val uiState: StateFlow<RepoUiState> = _uiState.asStateFlow()

    fun onSearchTextChanged(newSearchText: String) {
        _uiState.update { it.copy(searchText = newSearchText) }
    }

    fun requestRepoList() {
        val query = _uiState.value.searchText
        if (query.isBlank() || _uiState.value.isLoading) return

        viewModelScope.launch {
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
        return originResponse.find { it.id == id }?.toDetailModel()
    }
}

data class RepoUiState(
    val searchText: String = "",
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
    val repos: List<MainRepoModel> = emptyList(),
    val error: String? = null
)