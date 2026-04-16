package com.example.searchrepo.ui.screen.detail

import com.example.searchrepo.ui.model.RepoUiModel
import kotlinx.serialization.Serializable

@Serializable
data class DetailRepoModel(
    val id : Int = 0,
    val projectName: String = "",
    val description: String? = null,
    val language: String? = null,
    val stargazersCount: Int = 0,
    val forksCount: Int = 0,
    val userName: String = "",
    val avatarUrl: String = "",
    val updatedAt: String = "",
    val topics: List<String> = emptyList(),
    val watchersCount: Int = 0,
    val openIssuesCount: Int = 0,
    val license: String? = null,
    val defaultBranch: String = "",
    val htmlUrl: String = "",
    val createdAt: String = "",
    val isFavorite: Boolean = false
){
}

fun DetailRepoModel.toUiModel(): RepoUiModel {
    return RepoUiModel(
        id = this.id,
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
        licenseName = this.license,
        defaultBranch = this.defaultBranch,
        htmlUrl = this.htmlUrl,
        createdAt = this.createdAt
    )
}