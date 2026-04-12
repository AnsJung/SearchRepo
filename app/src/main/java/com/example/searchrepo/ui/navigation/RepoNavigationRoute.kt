package com.example.searchrepo.ui.navigation

import com.example.searchrepo.ui.screen.detail.DetailRepoModel
import kotlinx.serialization.Serializable


sealed interface Route{
    @Serializable
    object Main

    @Serializable
    data class Detail(val detailRepoModel: DetailRepoModel)
}
