package com.example.searchrepo.ui.screen.favorite

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.searchrepo.R
import com.example.searchrepo.ui.components.FavoriteItem
import com.example.searchrepo.ui.screen.detail.DetailRepoModel

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    isDarkMode: Boolean,
    onChangeTheme: () -> Unit,
    onNavigateToDetail: (DetailRepoModel) -> Unit
) {
    val themeIcon = if (isDarkMode) R.drawable.ic_set_light else R.drawable.ic_set_dark
    val themeContentDescription = if (isDarkMode) "라이트 모드" else "다크 모드"
    val favoriteRepos by viewModel.favoriteRepos.collectAsState()
    val context = LocalContext.current

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
            )
            {
                Text(
                    "즐겨찾기",
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
            Spacer(
                Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xffa1a1a1))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (favoriteRepos.isNotEmpty()) {
                    FavoriteList(
                        favoriteRepos,
                        onNavigateToDetail = { id ->
                            val detailRepoModel = viewModel.getDetailModel(id)

                            if (detailRepoModel != null) {
                                onNavigateToDetail(detailRepoModel)
                            } else {
                                // 💡 캐시에 데이터가 없는 등 실패 시 사용자 알림
                                Toast.makeText(context, "상세 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        removeFavorite = { id ->
                            viewModel.removeFavorite(id)
                        })
                } else {
                    Text(
                        "좋아요한 레포지토리가 없습니다\n레포지토리를 검색하고 좋아요를 눌러보세요",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(50.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun FavoriteList(
    favoriteRepos: List<FavoriteRepoModel>,
    onNavigateToDetail: (Int) -> Unit,
    removeFavorite: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = 50.dp,
            top = 5.dp
        )
    ) {
        items(favoriteRepos, key = { it.id }) { favoriteRepo ->
            FavoriteItem(
                favoriteRepo,
                Modifier.clickable { onNavigateToDetail(favoriteRepo.id) },
                removeFavorite = {
                    removeFavorite(favoriteRepo.id)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteScreenPreview() {
    MaterialTheme {
        FavoriteScreen(
            isDarkMode = false,
            onChangeTheme = {},
            onNavigateToDetail = {}
        )
    }
}