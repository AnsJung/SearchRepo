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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.searchrepo.R
import com.example.searchrepo.ui.components.CustomDialog
import com.example.searchrepo.ui.components.RepoItem
import com.example.searchrepo.ui.components.SearchTextField
import com.example.searchrepo.ui.navigation.Route
import com.example.searchrepo.ui.navigation.util.createNavType
import com.example.searchrepo.ui.screen.detail.DetailRepoModel
import com.example.searchrepo.ui.screen.detail.DetailScreen
import com.example.searchrepo.ui.theme.SearchRepoTheme
import kotlinx.coroutines.flow.flowOf
import kotlin.reflect.typeOf

@Composable
fun SearchRepoApp(viewModel: MainViewModel = hiltViewModel()) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val navController = rememberNavController()

    SearchRepoTheme(darkTheme = isDarkMode) {
        NavHost(navController = navController, startDestination = Route.Main) {
            composable<Route.Main> {
                MainScreen(
                    isDarkMode = isDarkMode,
                    onNavigateToDetail = { detailRepoModel ->
                        navController.navigate(Route.Detail(detailRepoModel))
                    },
                    onChangeTheme = {
                        viewModel.setDarkMode(!isDarkMode)
                    })
            }
            composable<Route.Detail>(
                typeMap = mapOf(
                    typeOf<DetailRepoModel>() to createNavType<DetailRepoModel>()
                )
            ) { backstackEntry ->
                val detailRoute = backstackEntry.toRoute<Route.Detail>()
                DetailScreen(detailRoute.detailRepoModel) {
                    navController.popBackStack()
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToDetail: (DetailRepoModel) -> Unit,
    isDarkMode: Boolean,
    onChangeTheme: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()
    val context = LocalContext.current
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
                Toast.makeText(context, "다음 화면으로 이동할 수 없습니다.", Toast.LENGTH_SHORT)
                    .show()
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
            title = "알림",
            message = "지울 내용이 없습니다.\n먼저 키워드를 입력해 보세요."
        )
    }
}

@Composable
private fun MainContent(
    pagingItems: LazyPagingItems<MainRepoModel>,
    state: MainUiState,
    isDarkMode: Boolean,
    onSearchTextChanged: (String) -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onRefreshSearched: () -> Unit,
    onChangeTheme: () -> Unit
) {
    val themeIcon = if (isDarkMode) R.drawable.ic_set_light else R.drawable.ic_set_dark
    val themeContentDescription = if (isDarkMode) "라이트 모드" else "다크 모드"
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
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "GitHub 레포지토리 검색",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
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
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (isDarkMode) Color.Gray else Color.Black
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
                state,
                onSearchTextChanged = onSearchTextChanged,
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
                    GuideText(text = "에러 발생: ${errorState.error.localizedMessage}")
                } else {
                    // 4. 가이드 텍스트 처리
                    if (!state.hasSearched) {
                        GuideText("검색어를 입력해주세요")
                    }

                    // 검색 결과가 없는 경우 (로딩 중이 아니고, 아이템 개수가 0일 때)
                    if (state.hasSearched &&
                        loadState.refresh is LoadState.NotLoading &&
                        pagingItems.itemCount == 0
                    ) {
                        GuideText("검색 결과가 없습니다")
                    }
                }
            }
        }
    }
}

@Composable
fun BoxScope.GuideText(text: String) {
    Text(
        text,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 18.sp,
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(50.dp)
    )
}

@Composable
fun RepoList(
    pagingItems: LazyPagingItems<MainRepoModel>,
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
                MainRepoModel(1, "Sample", "Javas", "Java", 5000, 1000, "Jun", "", "")
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