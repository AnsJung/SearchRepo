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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GitHubDarkAccentBlue,
    onPrimary = Color.Black,

    background = GitHubDarkBg,
    onBackground = GitHubDarkTextPrimary,

    surface = GitHubDarkSurface,
    onSurface = GitHubDarkTextPrimary,

    surfaceVariant = GitHubDarkSurface,
    onSurfaceVariant = GitHubDarkTextSecondary,

    outline = GitHubDarkBorder,
    outlineVariant = GitHubDarkBorder,

    secondaryContainer = GitHubDarkTagBg,
    onSecondaryContainer = GitHubDarkTagText,

    tertiary = GitHubDarkTFBg
)

private val LightColorScheme = lightColorScheme(
    primary = GitHubBlue,
    onPrimary = Color.White,

    background = Color.White,
    onBackground = GitHubDark,

    surface = Color.White,
    onSurface = GitHubDark,

    surfaceVariant = GitHubBgLight,
    onSurfaceVariant = GitHubSecondary,

    outline = GitHubBorder,
    outlineVariant = GitHubBorder,

    secondaryContainer = GitHubTopicTagBg,
    onSecondaryContainer = GitHubTopicTagText,

    tertiary = Color.White
)
@Composable
fun SearchRepoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // GitHub 테마를 고정해서 사용하기 위해 기본값을 false로 변경하는 것을 권장합니다.
    dynamicColor: Boolean = false,
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

    // 상태바 색상 제어 로직 추가
    val view = LocalView.current
// Theme.kt의 SearchRepoTheme 내부
    SideEffect {
        val window = (view.context as Activity).window
        val insetsController = WindowCompat.getInsetsController(window, view)

        // 배경색 설정은 이제 의미가 없으므로 아이콘 색상에만 집중합니다.
        insetsController.isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}