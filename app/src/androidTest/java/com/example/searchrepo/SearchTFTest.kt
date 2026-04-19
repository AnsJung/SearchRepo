package com.example.searchrepo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.example.searchrepo.ui.components.RepoItem
import com.example.searchrepo.ui.components.SearchTextField
import com.example.searchrepo.ui.model.RepoUiModel
import org.junit.Rule
import org.junit.Test

class SearchTFTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun checkShowPlaceHolder() {
        composeTestRule.setContent {
            SearchTextField(
                value = "",
                onValueChange = {}
            )
        }

        composeTestRule
            .onNodeWithText("레포지토리 검색...")
            .assertIsDisplayed()
    }


    @Test
    fun inputText() {
        var text = ""

        composeTestRule.setContent {
            SearchTextField(
                value = text,
                onValueChange = { text = it }
            )
        }

        composeTestRule
            .onNodeWithText("레포지토리 검색...")
            .performTextInput("android")

        composeTestRule
            .onNodeWithText("android")
            .assertIsDisplayed()
    }


    @Test
    fun checkData() {
        composeTestRule.setContent {
            RepoItem(
                item = RepoUiModel(
                    id = 1,
                    projectName = "SearchRepo",
                    description = "테스트 설명",
                    language = "Kotlin",
                    stargazersCount = 1000,
                    forksCount = 100,
                    userName = "jun",
                    avatarUrl = "",
                    updatedAt = "2026-02-26T08:25:15Z"
                )
            )
        }

        composeTestRule.onNodeWithText("SearchRepo").assertIsDisplayed()
        composeTestRule.onNodeWithText("jun").assertIsDisplayed()
    }
}