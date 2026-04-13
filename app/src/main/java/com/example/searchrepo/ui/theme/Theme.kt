package com.example.searchrepo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = GitHubBlue,                // 프로젝트 이름 (Blue)
    onPrimary = Color.White,

    background = Color.White,            // 전체 배경
    onBackground = GitHubDark,           // 메인 텍스트 ("GitHub 레포지토리 검색", 유저명 등)

    surfaceVariant = GitHubBgLight,      // 리스트 영역 배경 (연한 회색)
    onSurfaceVariant = GitHubSecondary,  // 보조 텍스트 (날짜, 스타 수, 가이드 문구)

    outlineVariant = GitHubBorder,        // 아이템 테두리 선

    secondaryContainer = GitHubTopicTagBg,
    onSecondaryContainer = GitHubTopicTagText,
)

@Composable
fun SearchRepoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}