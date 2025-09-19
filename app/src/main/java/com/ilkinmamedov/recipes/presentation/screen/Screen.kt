package com.ilkinmamedov.recipes.presentation.screen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{id}") {
        fun createRoute(id: Long) = "detail/$id"
    }
}