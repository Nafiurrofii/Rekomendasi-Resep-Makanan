package com.rekomendasiresepmakanan.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rekomendasiresepmakanan.ui.screen.detail.DetailScreen
import com.rekomendasiresepmakanan.ui.screen.home.HomeScreen
import com.rekomendasiresepmakanan.ui.screen.search.SearchScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToSearch = { navController.navigate("search") },
                onNavigateToDetail = { recipeId ->
                    navController.navigate("detail/$recipeId")
                }
            )
        }
        
        composable("search") {
            SearchScreen(
                onNavigateHome = {
                    navController.popBackStack("home", inclusive = false)
                },
                 onNavigateToDetail = { recipeId ->
                    navController.navigate("detail/$recipeId")
                }
            )
        }

        composable(
            route = "detail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) {
            // ViewModel akan di-instance dengan recipeId yang diambil dari NavHost
            DetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
