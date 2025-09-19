package com.ilkinmamedov.domain.repository

import com.ilkinmamedov.domain.model.Recipe

sealed class RecipesResult {
    data class Success(val recipes: List<Recipe>) : RecipesResult()
    data class Failure(val code: Int, val message: String) : RecipesResult()
}