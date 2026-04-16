package com.example.searchrepo.ui.model

import com.example.searchrepo.ui.screen.detail.DetailRepoModel
import com.example.searchrepo.ui.screen.favorite.FavoriteRepoModel
import com.example.searchrepo.ui.screen.main.MainRepoModel
import kotlinx.serialization.Serializable

@Serializable
data class RepoUiModel(
    val id: Int,
    val projectName: String,
    val description: String?,
    val htmlUrl: String,
    val language: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val userName: String,
    val avatarUrl: String,
    val updatedAt: String,
    val topics: List<String>,
    val watchersCount: Int,
    val openIssuesCount: Int,
    val licenseName: String?,
    val defaultBranch: String,
    val createdAt: String,
)

fun RepoUiModel.toMainModel(): MainRepoModel {
    return MainRepoModel(
        id = this.id,
        projectName = this.projectName,
        description = this.description,
        language = this.language,
        stargazersCount = this.stargazersCount,
        forksCount = this.forksCount,
        userName = this.userName,
        avatarUrl = this.avatarUrl,
        updatedAt = this.updatedAt
    )
}

fun RepoUiModel.toDetailModel(): DetailRepoModel {
    return DetailRepoModel(
        id = id,
        projectName = this.projectName,
        description = this.description,
        language = this.language,
        stargazersCount = this.stargazersCount,
        forksCount = this.forksCount,
        userName = this.userName,
        avatarUrl = this.avatarUrl,
        updatedAt = this.updatedAt,
        topics = this.topics,
        watchersCount = this.watchersCount,
        openIssuesCount = this.openIssuesCount,
        license = this.licenseName,
        defaultBranch = this.defaultBranch,
        htmlUrl = this.htmlUrl,
        createdAt = this.createdAt,
    )
}

fun RepoUiModel.toFavoriteModel(): FavoriteRepoModel {
    return FavoriteRepoModel(
        id = this.id,
        projectName = this.projectName,
        description = this.description,
        language = this.language,
        stargazersCount = this.stargazersCount,
        forksCount = this.forksCount,
        userName = this.userName,
        avatarUrl = this.avatarUrl,
        updatedAt = this.updatedAt
    )
}
