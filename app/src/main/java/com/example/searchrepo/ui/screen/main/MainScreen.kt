package com.example.searchrepo.ui.screen.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.searchrepo.R
import com.example.searchrepo.ui.components.CustomDialog
import com.example.searchrepo.ui.components.RepoItem
import com.example.searchrepo.ui.components.SearchTextField
import com.example.searchrepo.ui.model.RepoUiModel
import com.example.searchrepo.ui.screen.detail.DetailRepoModel
import com.example.searchrepo.ui.theme.SearchRepoTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToDetail: (DetailRepoModel) -> Unit,
    isDarkMode: Boolean,
    onChangeTheme: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()
    val context = LocalContext.current
    val detailNotFoundMessage = stringResource(R.string.error_detail_not_found)
    MainContent(
        pagingItems,
        state,
        isDarkMode,
        onSearchTextChanged = viewModel::onSearchTextChange,
        onNavigateToDetail = { id ->
            val detailRepoModel = viewModel.getDetailItem(id)

            if (detailRepoModel != null) {
                onNavigateToDetail(detailRepoModel)
            } else {
                Toast.makeText(
                    context,
                    detailNotFoundMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        onRefreshSearched = viewModel::refreshSearched,
        onChangeTheme = onChangeTheme
    )
    if (state.showGuideDialog) {
        CustomDialog(
            onDismissRequest = {
                viewModel.onDialogDismiss()
            },
            onConfirm = {
                viewModel.onDialogDismiss()
            },
            confirmBtnColor = MaterialTheme.colorScheme.primary,
            title = stringResource(R.string.dialog_title_notice),
            message = stringResource(R.string.search_clear_guide_message)
        )
    }
}

@Composable
private fun MainContent(
    pagingItems: LazyPagingItems<RepoUiModel>,
    state: MainUiState,
    isDarkMode: Boolean,
    onSearchTextChanged: (String) -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onRefreshSearched: () -> Unit,
    onChangeTheme: () -> Unit
) {
    val themeIcon = if (isDarkMode) R.drawable.ic_set_light else R.drawable.ic_set_dark
    val themeContentDescription = if (isDarkMode) {
        stringResource(R.string.cd_switch_to_light_mode)
    } else {
        stringResource(R.string.cd_switch_to_dark_mode)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.onPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.main_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            onRefreshSearched()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_refresh),
                        contentDescription = stringResource(R.string.cd_refresh),
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            onChangeTheme()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(themeIcon),
                        contentDescription = themeContentDescription,
                        modifier = Modifier.size(20.dp),
                        tint = if (isDarkMode) Color(0xffFBBF24) else Color.Unspecified
                    )
                }
                Spacer(Modifier.width(10.dp))
            }

            SearchTextField(
                state.searchText,
                onValueChange = onSearchTextChanged,
            )
            Spacer(
                Modifier
                    .height(15.dp)
                    .background(MaterialTheme.colorScheme.background)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                RepoList(pagingItems, onNavigateToDetail)
                val loadState = pagingItems.loadState
                if (loadState.refresh is LoadState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                val errorState = loadState.source.refresh as? LoadState.Error
                    ?: loadState.source.append as? LoadState.Error
                    ?: loadState.refresh as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                if (errorState != null) {
                    GuideText(
                        text = stringResource(
                            R.string.error_occurred_with_message,
                            errorState.error.localizedMessage ?: ""
                        )
                    )
                } else {
                    // 4. 가이드 텍스트 처리
                    if (!state.hasSearched) {
                        GuideText(stringResource(R.string.empty_search))
                    }

                    // 검색 결과가 없는 경우 (로딩 중이 아니고, 아이템 개수가 0일 때)
                    if (state.hasSearched &&
                        loadState.refresh is LoadState.NotLoading &&
                        pagingItems.itemCount == 0
                    ) {
                        GuideText(stringResource(R.string.no_result))
                    }
                }
            }
        }
    }
}

@Composable
fun BoxScope.GuideText(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(50.dp)
    )
}

@Composable
fun RepoList(
    pagingItems: LazyPagingItems<RepoUiModel>,
    onNavigateToDetail: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = 50.dp,
            top = 5.dp
        )
    ) {
        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey { repo -> repo.id })
        { index ->
            val repo = pagingItems[index]
            if (repo != null) {
                RepoItem(repo, Modifier.clickable { onNavigateToDetail(repo.id) })
            }
        }

        if (pagingItems.loadState.append is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // 1. 가로 전체를 채우고
                        .padding(16.dp),
                    contentAlignment = Alignment.Center // 2. 내용을 중앙에 배치
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp), // 크기 조절 (선택)
                        strokeWidth = 3.dp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    // Preview를 위한 가짜 페이징 데이터 생성
    val fakeData = flowOf(
        PagingData.from(
            listOf(
                RepoUiModel(1, "Sample", "Javas", "Java", 5000, 1000, "Jun", "", "")
            )
        )
    ).collectAsLazyPagingItems()

    SearchRepoTheme {
        MainContent(
            pagingItems = fakeData, // 추가된 파라미터
            state = MainUiState(hasSearched = true),
            isDarkMode = false,
            onSearchTextChanged = {},
            onNavigateToDetail = {},
            onRefreshSearched = {},
            onChangeTheme = {},
        )
    }
}