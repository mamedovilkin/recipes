package com.ilkinmamedov.domain.repository

import com.ilkinmamedov.domain.model.SearchRecipe
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    suspend fun getRandomRecipes(): Flow<RecipesResult>

    suspend fun getRecipeDetail(id: Long): Flow<DetailRecipeResult>

    suspend fun searchRecipes(query: String): Flow<List<SearchRecipe>>

    suspend fun getSimilarRecipes(id: Long): Flow<List<SearchRecipe>?>
}