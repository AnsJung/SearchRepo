package com.example.searchrepo.ui.screen.favorite

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.searchrepo.R
import com.example.searchrepo.ui.components.AppTopBar
import com.example.searchrepo.ui.components.RepoItem
import com.example.searchrepo.ui.model.RepoUiModel
import com.example.searchrepo.ui.screen.detail.DetailRepoModel

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    isDarkMode: Boolean,
    onChangeTheme: () -> Unit,
    onNavigateToDetail: (DetailRepoModel) -> Unit
) {
    val themeIcon = if (isDarkMode) R.drawable.ic_set_light else R.drawable.ic_set_dark
    val themeContentDescription = if (isDarkMode) {
        stringResource(R.string.cd_switch_to_light_mode)
    } else {
        stringResource(R.string.cd_switch_to_dark_mode)
    }
    val favoriteRepos by viewModel.favoriteRepos.collectAsState()
    val context = LocalContext.current
    val detailNotFoundMessage = stringResource(R.string.error_detail_not_found)
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
            AppTopBar(
                title = stringResource(R.string.favorite_title),
                actions = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { onChangeTheme() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(themeIcon),
                            contentDescription = themeContentDescription,
                            modifier = Modifier.size(20.dp),
                            tint = if (isDarkMode) Color(0xffFBBF24) else Color.Unspecified
                        )
                    }
                }
            )
            Spacer(
                Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant)
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
                                Toast.makeText(
                                    context,
                                    detailNotFoundMessage,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        },
                        removeFavorite = { id ->
                            viewModel.removeFavorite(id)
                        })
                } else {
                    Text(
                        text = stringResource(R.string.favorite_empty_message),
                        style = MaterialTheme.typography.bodyLarge,
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
    favoriteRepos: List<RepoUiModel>,
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
            RepoItem(
                item = favoriteRepo,
                modifier = Modifier.clickable { onNavigateToDetail(favoriteRepo.id) },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_git_heart_filled),
                    contentDescription = stringResource(R.string.cd_remove_favorite),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            removeFavorite(favoriteRepo.id)
                        },
                    tint = Color.Unspecified
                )
            }
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