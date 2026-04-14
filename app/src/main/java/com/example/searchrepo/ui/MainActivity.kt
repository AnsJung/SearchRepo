package com.example.searchrepo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.searchrepo.ui.navigation.Route
import com.example.searchrepo.ui.navigation.util.createNaveType
import com.example.searchrepo.ui.screen.detail.DetailRepoModel
import com.example.searchrepo.ui.screen.detail.DetailScreen
import com.example.searchrepo.ui.screen.main.MainScreen
import com.example.searchrepo.ui.theme.SearchRepoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(
                false) }
            LaunchedEffect(isDarkMode) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        lightScrim = android.graphics.Color.TRANSPARENT,
                        darkScrim = android.graphics.Color.TRANSPARENT,
                        detectDarkMode = { isDarkMode } // 현재 모드 강제 지정
                    )
                )
            }
            SearchRepoTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Route.Main) {
                    composable<Route.Main> {
                        MainScreen(
                            isDarkMode = isDarkMode,
                            onNavigateToDetail = { detailRepoModel ->
                                navController.navigate(Route.Detail(detailRepoModel))
                            },
                            onChangeTheme = {
                                isDarkMode = !isDarkMode
                            })
                    }
                    composable<Route.Detail>(
                        typeMap = mapOf(
                            typeOf<DetailRepoModel>() to createNaveType<DetailRepoModel>()
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
    }
}
