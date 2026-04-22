package com.example.searchrepo.ui.model

import com.example.searchrepo.data.local.FavoriteRepoPreference
import com.example.searchrepo.ui.screen.detail.DetailRepoModel

fun RepoOriginModel.toFavoritePreference(): FavoriteRepoPreference {
    return FavoriteRepoPreference(
        id = id,
        projectName = projectName,
        htmlUrl = htmlUrl,
        userName = userName,
        avatarUrl = avatarUrl,
        description = description,
        language = language,
        stargazersCount = stargazersCount,
        forksCount = forksCount,
        updatedAt = updatedAt,
        topics = topics,
        watchersCount = watchersCount,
        openIssuesCount = openIssuesCount,
        licenseName = licenseName,
        defaultBranch = defaultBranch,
        createdAt = createdAt
    )
}

fun FavoriteRepoPreference.toFavoriteModel(): RepoUiModel {
    return RepoUiModel(
        id = id,
        projectName = projectName,
        description = description,
        language = language,
        stargazersCount = stargazersCount,
        forksCount = forksCount,
        userName = userName,
        avatarUrl = avatarUrl,
        updatedAt = updatedAt
    )
}

fun FavoriteRepoPreference.toDetailModel(): DetailRepoModel {
    return DetailRepoModel(
        id = id,
        projectName = projectName,
        description = description,
        language = language,
        stargazersCount = stargazersCount,
        forksCount = forksCount,
        userName = userName,
        avatarUrl = avatarUrl,
        updatedAt = updatedAt,
        topics = topics,
        watchersCount = watchersCount,
        openIssuesCount = openIssuesCount,
        license = licenseName,
        defaultBranch = defaultBranch,
        htmlUrl = htmlUrl,
        createdAt = createdAt
    )
}

