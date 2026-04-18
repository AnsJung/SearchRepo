package com.example.searchrepo.ui.screen.detail

import android.content.Intent
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.searchrepo.R
import com.example.searchrepo.ui.util.GithubUtil
import com.example.searchrepo.ui.util.toKoreanDate
import com.example.searchrepo.ui.util.toShortenedString

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
) {
    val isFavorite by detailViewModel.isFavorite.collectAsState()
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(MaterialTheme.colorScheme.background),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.cd_back),
                        modifier = Modifier
                            .clickable {
                                onBackClick()
                            }
                            .padding(start = 15.dp)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = detailViewModel.detailRepoModel.projectName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(Modifier.weight(1f))
                    Icon(
                        painter =
                            if (isFavorite) {
                                painterResource(R.drawable.ic_git_heart_filled)
                            } else {
                                painterResource(R.drawable.ic_git_heart_border)
                            },
                        contentDescription = stringResource(R.string.cd_toggle_favorite),
                        Modifier
                            .size(20.dp)
                            .clickable {
                                detailViewModel.toggleFavoriteItem()
                            },
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(20.dp))
                }
            })
        { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(top = paddingValues.calculateTopPadding())
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 15.dp)
                    .padding(top = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OwnerArea(
                    detailViewModel.detailRepoModel.avatarUrl,
                    detailViewModel.detailRepoModel.userName
                )
                if (!detailViewModel.detailRepoModel.description.isNullOrBlank()) {
                    DescriptionArea(detailViewModel.detailRepoModel.description)
                }
                if (detailViewModel.detailRepoModel.topics.isNotEmpty()) {
                    TopicsArea(detailViewModel.detailRepoModel.topics)
                }
                CountArea(
                    detailViewModel.detailRepoModel.stargazersCount,
                    detailViewModel.detailRepoModel.forksCount,
                    detailViewModel.detailRepoModel.watchersCount,
                    detailViewModel.detailRepoModel.openIssuesCount
                )
                InfoArea(
                    detailViewModel.detailRepoModel.language,
                    detailViewModel.detailRepoModel.defaultBranch,
                    detailViewModel.detailRepoModel.license,
                    detailViewModel.detailRepoModel.createdAt,
                    detailViewModel.detailRepoModel.updatedAt
                )
                MoveArea(
                    detailViewModel.detailRepoModel.htmlUrl
                )
            }
        }
    }
}

@Composable
fun DetailSectionContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun OwnerArea(avatarUrl: String, userName: String) {
    var isImageVisible by remember { mutableStateOf(true) }
    DetailSectionContainer {
        Row {
            if (isImageVisible) {
                AsyncImage(
                    model = avatarUrl,
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(R.string.cd_user_avatar),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    onError = { error ->
                        Log.e("JH", error.result.throwable.toString())
                        isImageVisible = false
                    }
                )

            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = stringResource(R.string.detail_owner),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
fun DescriptionArea(description: String) {
    DetailSectionContainer {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun TopicsArea(topics: List<String>) {
    DetailSectionContainer {
        Column {
            Text(
                text = stringResource(R.string.detail_topics),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(10.dp))
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                topics.forEach { text ->
                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = CircleShape
                            )
                            .padding(horizontal = 10.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CountArea(
    stargazersCount: Int,
    forksCount: Int,
    watchersCount: Int,
    openIssuesCount: Int
) {
    DetailSectionContainer {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                StatItem(
                    iconResId = R.drawable.ic_git_star,
                    label = "Stars",
                    value = stargazersCount,
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    iconResId = R.drawable.ic_git_fork,
                    iconColor = Color.Gray,
                    label = "Forks",
                    value = forksCount,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                StatItem(
                    iconResId = R.drawable.ic_git_eye,
                    iconColor = Color.Gray,
                    label = "Watchers",
                    value = watchersCount,
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    iconResId = R.drawable.ic_issues,
                    iconColor = Color.Gray,
                    label = "Issues",
                    value = openIssuesCount,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StatItem(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int,
    iconColor: Color? = null,
    label: String,
    value: Int,
) {
    Row(
        modifier = modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = iconColor ?: Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = value.toShortenedString(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun InfoArea(
    language: String?,
    defaultBranch: String,
    license: String?,
    createdAt: String,
    updatedAt: String
) {
    DetailSectionContainer {
        Column {
            language?.let {
                InfoRow(
                    languageColor = GithubUtil.getLanguageColor(it),
                    label = stringResource(R.string.detail_language),
                    value = it
                )
            }

            InfoRow(
                iconResId = R.drawable.ic_git_branch,
                label = stringResource(R.string.detail_default_branch),
                value = defaultBranch
            )

            license?.let {
                InfoRow(
                    iconResId = R.drawable.ic_issues,
                    label = stringResource(R.string.detail_license),
                    value = it
                )
            }

            InfoRow(
                iconResId = R.drawable.ic_calendar,
                label = stringResource(R.string.detail_created_at),
                value = createdAt.toKoreanDate()
            )

            InfoRow(
                iconResId = R.drawable.ic_calendar,
                label = stringResource(R.string.detail_updated_at),
                value = updatedAt.toKoreanDate()
            )
        }
    }
}

@Composable
fun InfoRow(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int? = null,
    languageColor: Color? = null, // 첫 번째 항목의 파란 점을 위한 처리
    label: String,
    value: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 아이콘 혹은 언어 색상 점 표시
        Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
            if (languageColor != null) {
                // '언어' 항목의 경우 파란색 원형 점
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(color = languageColor, shape = CircleShape)
                )
            } else if (iconResId != null) {
                // 나머지 항목의 경우 SVG 아이콘
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant, // 아이콘은 타이틀과 같은 회색톤
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "$label:",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(
            text = value,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun MoveArea(htmlUrl: String) {
    val context = LocalContext.current
    DetailSectionContainer(
        modifier = Modifier.clickable {
            val intent = Intent(
                Intent.ACTION_VIEW,
                htmlUrl.toUri()
            )
            context.startActivity(intent)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.detail_view_on_github),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(R.drawable.ic_external_link),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = stringResource(R.string.cd_move_to_page),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "GitHub 상세 화면 프리뷰")
@Composable
private fun DetailScreenPreview() {
    MaterialTheme {
        DetailScreen(
            onBackClick = { /* 테스트 시 로그 출력 등 */ }
        )
    }
}