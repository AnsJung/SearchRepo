package com.example.searchrepo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.searchrepo.ui.navigation.BottomNavItem
import com.example.searchrepo.ui.navigation.Route
import com.example.searchrepo.ui.navigation.util.createNavType
import com.example.searchrepo.ui.screen.detail.DetailRepoModel
import com.example.searchrepo.ui.screen.detail.DetailScreen
import com.example.searchrepo.ui.screen.favorite.FavoriteScreen
import com.example.searchrepo.ui.screen.main.MainScreen
import com.example.searchrepo.ui.screen.main.MainViewModel
import com.example.searchrepo.ui.theme.GitHubSecondary
import com.example.searchrepo.ui.theme.SearchRepoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SearchRepoApp()
        }
    }

    @Composable
    fun SearchRepoApp(viewModel: MainViewModel = hiltViewModel()) {
        val isDarkMode by viewModel.isDarkMode.collectAsState()
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        SearchRepoTheme(darkTheme = isDarkMode) {
            Scaffold(bottomBar = {
                if (currentRoute?.contains("Detail") == false) {
                    MyNavigationBar(navController, currentRoute)
                }
            }) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = Route.Main,
                    modifier = Modifier.padding(
                        bottom = paddingValues.calculateBottomPadding()
                    )
                ) {
                    composable<Route.Main> {
                        MainScreen(
                            viewModel,
                            isDarkMode = isDarkMode,
                            onNavigateToDetail = { detailRepoModel ->
                                navController.navigate(Route.Detail(detailRepoModel))
                            },
                            onChangeTheme = {
                                viewModel.setDarkMode(!isDarkMode)
                            })
                    }
                    composable<Route.Favorite> {
                        FavoriteScreen(
                            isDarkMode = isDarkMode,
                            onChangeTheme = {
                                viewModel.setDarkMode(!isDarkMode)
                            },
                            onNavigateToDetail = { detailRepoModel ->
                                navController.navigate(Route.Detail(detailRepoModel))
                            })
                    }
                    composable<Route.Detail>(
                        typeMap = mapOf(
                            typeOf<DetailRepoModel>() to createNavType<DetailRepoModel>()
                        )
                    ) {
                        DetailScreen(
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }

        }
    }

    @Composable
    fun MyNavigationBar(
        navController: NavHostController,
        currentRoute: String?
    ) {
        val items = listOf(BottomNavItem.Search, BottomNavItem.Favorite)

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route::class.qualifiedName
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text(text = item.name) },
                    icon = {
                        Icon(
                            painter = painterResource(if (isSelected) item.selectedIcon else item.unselectedIcon),
                            contentDescription = item.name
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Color(0xff155dfc),
                        selectedTextColor = Color(0xff155dfc),
                        unselectedTextColor = GitHubSecondary,
                        unselectedIconColor = GitHubSecondary,
                    )
                )
            }
        }
    }
}
