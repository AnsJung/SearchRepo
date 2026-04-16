package com.example.searchrepo.ui.navigation

import com.example.searchrepo.R

sealed class BottomNavItem(
    val route: Any,
    val name: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    object Search : BottomNavItem(
        route = Route.Main,
        name = "Search",
        selectedIcon = R.drawable.ic_git_search_select, // 미리 준비된 아이콘
        unselectedIcon = R.drawable.ic_git_search
    )
    object Favorite : BottomNavItem(
        route = Route.Favorite, // 나중에 추가할 즐겨찾기 화면
        name = "즐겨찾기",
        selectedIcon = R.drawable.ic_git_favorite_select,
        unselectedIcon = R.drawable.ic_git_favorite
    )
}