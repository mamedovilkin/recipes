package com.ilkinmamedov.domain.usecase

import com.ilkinmamedov.domain.repository.RecipesRepository

class DetailUseCase(
    private val recipesRepository: RecipesRepository
) {

    suspend fun getRecipeDetail(id: Long) = recipesRepository.getRecipeDetail(id)

    suspend fun getSimilarRecipes(id: Long) = recipesRepository.getSimilarRecipes(id)

}