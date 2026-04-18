package com.example.searchrepo.ui.model

import com.example.searchrepo.ui.screen.detail.DetailRepoModel
import kotlinx.serialization.Serializable

@Serializable
data class RepoOriginModel(
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

fun RepoOriginModel.toMainModel(): RepoUiModel {
    return RepoUiModel(
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

fun RepoOriginModel.toDetailModel(): DetailRepoModel {
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

fun RepoOriginModel.toFavoriteModel(): RepoUiModel {
    return RepoUiModel(
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
