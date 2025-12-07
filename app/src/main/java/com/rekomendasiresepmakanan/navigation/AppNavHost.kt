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
import com.rekomendasiresepmakanan.ui.screen.category.CategoryScreen
import com.rekomendasiresepmakanan.ui.screen.category.CategoryDetailScreen
import com.rekomendasiresepmakanan.ui.screen.favorite.FavoriteScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToSearch = { navController.navigate("search") },
                onNavigateToDetail = { recipeId -> navController.navigate("detail/$recipeId") },
                onNavigateToCategories = { navController.navigate("categories") },
                onNavigateToCategoryDetail = { categoryName -> navController.navigate("category_detail/$categoryName") },
                onNavigateToFavorite = { navController.navigate("favorite") }
            )
        }
        
        composable("search") {
            SearchScreen(
                onNavigateHome = { navController.popBackStack("home", inclusive = false) },
                onNavigateToDetail = { recipeId -> navController.navigate("detail/$recipeId") },
                onNavigateToFavorite = { navController.navigate("favorite") }
            )
        }

        composable("favorite") {
            FavoriteScreen(
                onNavigateToHome = { navController.popBackStack("home", inclusive = false) },
                onNavigateToSearch = { navController.navigate("search") },
                onNavigateToDetail = { recipeId -> navController.navigate("detail/$recipeId") }
            )
        }

        composable("categories") {
            CategoryScreen(
                onBackClick = { navController.popBackStack() },
                onCategoryClick = { categoryName -> navController.navigate("category_detail/$categoryName") }
            )
        }

        composable(
            route = "category_detail/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryDetailScreen(
                categoryName = categoryName,
                onBackClick = { navController.popBackStack() },
                onRecipeClick = { recipeId -> navController.navigate("detail/$recipeId") }
            )
        }

        composable(
            route = "detail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
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
