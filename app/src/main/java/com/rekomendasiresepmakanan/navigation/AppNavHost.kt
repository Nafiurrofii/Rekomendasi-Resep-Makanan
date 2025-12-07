package com.rekomendasiresepmakanan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rekomendasiresepmakanan.ui.screen.home.HomeScreen
import com.rekomendasiresepmakanan.ui.screen.search.SearchScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToSearch = { navController.navigate("search") }
            )
        }
        
        composable("search") {
            SearchScreen(
                onNavigateHome = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }
    }
}
