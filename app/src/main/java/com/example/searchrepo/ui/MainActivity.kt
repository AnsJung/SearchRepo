package com.example.searchrepo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.searchrepo.ui.model.Detail
import com.example.searchrepo.ui.model.Main
import com.example.searchrepo.ui.theme.SearchRepoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SearchRepoTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Main) {
                    composable<Main>{
                        MainScreen(onNavigateToDetail = {
                            navController.navigate(Detail)
                        })
                    }
                    composable<Detail>{
                        DetailScreen()
                    }
                }
            }
        }
    }
}
