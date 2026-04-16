package com.example.searchrepo.ui.screen.favorite

data class FavoriteRepoModel(
    val id: Int,
    val projectName: String,
    val description: String?,
    val language: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val userName: String,
    val avatarUrl: String,
    val updatedAt: String
)