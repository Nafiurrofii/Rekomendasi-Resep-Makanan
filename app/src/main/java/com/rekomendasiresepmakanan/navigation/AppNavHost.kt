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
import com.rekomendasiresepmakanan.ui.screen.ingredients.IngredientsScreen
import com.rekomendasiresepmakanan.ui.screen.steps.StepsScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToSearch = { navController.navigate("search") },
                // Menambahkan navigasi ke detail saat item diklik
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
                // Menambahkan navigasi ke detail dari hasil pencarian
                onNavigateToDetail = { recipeId ->
                    navController.navigate("detail/$recipeId")
                }
            )
        }

        composable(
            route = "detail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            DetailScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToIngredients = { id -> navController.navigate("ingredients/$id") },
                onNavigateToSteps = { id -> navController.navigate("steps/$id") }
            )
        }

        composable(
            route = "ingredients/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            IngredientsScreen(
                recipeId = recipeId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = "steps/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            StepsScreen(
                recipeId = recipeId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
