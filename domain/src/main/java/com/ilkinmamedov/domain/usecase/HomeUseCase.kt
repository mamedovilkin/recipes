package com.ilkinmamedov.domain.usecase

import com.ilkinmamedov.domain.repository.RecipesRepository

class HomeUseCase(
    private val recipesRepository: RecipesRepository
) {

    suspend fun getAllRecipes() = recipesRepository.getRandomRecipes()

    suspend fun searchRecipes(query: String) = recipesRepository.searchRecipes(query)
}