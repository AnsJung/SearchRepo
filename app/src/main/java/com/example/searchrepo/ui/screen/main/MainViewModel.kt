package com.example.searchrepo.ui.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
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

    private val repoCache = mutableMapOf<Int, RepoUiModel>()
    private val _uiState = MutableStateFlow(RepoUiState())
    val uiState: StateFlow<RepoUiState> = _uiState.asStateFlow()
    private val _pagingData = MutableStateFlow<PagingData<MainRepoModel>>(PagingData.empty())
    val pagingData: StateFlow<PagingData<MainRepoModel>> = _pagingData.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState
                .map { it.searchText }
                .distinctUntilChanged()
                .onEach { query ->
                    if (query.isBlank()) {
                        _pagingData.value = PagingData.empty()
                        _uiState.update { it.copy(hasSearched = false) }
                    }
                }
                .debounce(500)
                .filter { it.isNotBlank() }
                .flatMapLatest { query ->
                    // 단순히 함수를 실행하는 게 아니라,
                    // Repository에서 만든 페이징 Flow를 이 흐름에 합칩니다.
                    githubRepository.getSearchRepoPaging(query)
                        .map { pagingData ->
                            pagingData.map { repoUiModel->
                                repoCache[repoUiModel.id] = repoUiModel
                                repoUiModel.toMainModel()
                            }
                        }
                        .cachedIn(viewModelScope)
                }
                .collect { pagingData ->
                    // 최종적으로 생성된 PagingData를 _pagingData에 업데이트
                    _pagingData.value = pagingData
                    _uiState.update { it.copy(hasSearched = true) }
                }
        }
    }

    fun onSearchTextChange(newSearchText: String) {
        _uiState.update { it.copy(searchText = newSearchText) }
    }

    fun getDetailItem(id: Int): DetailRepoModel? {
        // 캐시(Map)에서 ID로 원본 RepoUiModel을 찾아서 변환
        return repoCache[id]?.toDetailModel()
    }

    fun refreshSearched() {
        val currentState = _uiState.value
        if (currentState.searchText.isBlank() && !currentState.hasSearched) {
            _uiState.update { it.copy(showGuideDialog = true) }
            return
        }
        _uiState.update {
            it.copy(
                searchText = "",
                hasSearched = false
            )
        }
        _pagingData.value = PagingData.empty()
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
//    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
//    val error: String? = null,
    val showGuideDialog: Boolean = false
)