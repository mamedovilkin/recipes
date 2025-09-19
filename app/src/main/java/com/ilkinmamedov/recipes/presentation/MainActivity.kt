package com.ilkinmamedov.recipes.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ilkinmamedov.recipes.presentation.screen.Screen
import com.ilkinmamedov.recipes.presentation.screen.detail.DetailScreen
import com.ilkinmamedov.recipes.presentation.screen.detail.DetailViewModel
import com.ilkinmamedov.recipes.presentation.screen.home.HomeScreen
import com.ilkinmamedov.recipes.presentation.theme.RecipesTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipesTheme {
                RecipesApp()
            }
        }
    }
}

@Composable
@Preview
fun RecipesApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onOpenDetail = { id ->
                    navController.navigate(Screen.Detail.createRoute(id))
                }
            )
        }
        composable(
            Screen.Detail.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val detailViewModel: DetailViewModel = koinViewModel(
                parameters = { parametersOf(backStackEntry.arguments?.getLong("id")) }
            )

            DetailScreen(
                onBack = { navController.popBackStack() },
                onOpenOnWebsite = {
                    it?.let { url ->
                        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                    }
                },
                onOpenDetail = { id ->
                    navController.navigate(Screen.Detail.createRoute(id))
                },
                detailViewModel = detailViewModel
            )
        }
    }
}