package com.example.searchrepo.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.searchrepo.R
import com.example.searchrepo.ui.model.RepoUiModel
import com.example.searchrepo.ui.util.GithubUtil
import com.example.searchrepo.ui.util.toRelativeTime
import com.example.searchrepo.ui.util.toShortenedString

@Composable
fun RepoItem(item: RepoUiModel) {
    Column(
        Modifier
            .padding(vertical = 5.dp, horizontal = 15.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.avatarUrl)
                    .build(),
                error = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "유저 아이콘",
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                onError = { error ->
                    Log.e("JH", error.result.throwable.toString())
                }
            )
            Text(
                item.userName,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        Text(
            item.projectName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        item.description?.takeIf { it.isNotEmpty() }
            ?.let { desc ->
                Text(
                    text = desc,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2
                )
            }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item.language?.let { lang ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box( // Spacer보다 Box가 의도가 명확함
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(GithubUtil.getLanguageColor(lang))
                    )
                    Text(
                        text = " $lang",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 12.sp
                    )
                }
            }
            IconStat(
                icon = Icons.Outlined.Star,
                text = item.stargazersCount.toShortenedString()
            )
            IconStat(
                painter = painterResource(R.drawable.fork),
                text = item.forksCount.toShortenedString()
            )
            Spacer(Modifier.weight(1f))
            Text(
                item.updatedAt.toRelativeTime(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun IconStat(
    icon: ImageVector? = null,
    painter: Painter? = null,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val statColor = MaterialTheme.colorScheme.onSurfaceVariant // 7. 공통 아이콘/텍스트색

        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = statColor
            )
        } else if (painter != null) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = statColor
            )
        }
        Text(text = text, fontSize = 12.sp, color = statColor)
    }
}

@Preview(showBackground = true)
@Composable
private fun RepoItemPreview() {
    MaterialTheme {
        RepoItem(
            item =
                RepoUiModel(
                    id = 1,
                    projectName = "Test projectName",
                    fullName = "Test",
                    description = "Test description",
                    language = "Kotlin",
                    stargazersCount = 3000,
                    forksCount = 10000,
                    userName = "TestUserName",
                    avatarUrl = "",
                    updatedAt = "2026-02-26T08:25:15Z"
                )
        )
    }
}
