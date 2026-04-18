package com.example.searchrepo.ui.navigation

import com.example.searchrepo.R

sealed class BottomNavItem(
    val route: Any,
    val name: Int,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    object Search : BottomNavItem(
        route = Route.Main,
        name = R.string.nav_search,
        selectedIcon = R.drawable.ic_git_search_select,
        unselectedIcon = R.drawable.ic_git_search
    )
    object Favorite : BottomNavItem(
        route = Route.Favorite,
        name = R.string.nav_favorite,
        selectedIcon = R.drawable.ic_git_favorite_select,
        unselectedIcon = R.drawable.ic_git_favorite
    )
}