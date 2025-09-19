package com.ilkinmamedov.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipesDto(
    val recipes: List<RecipeDto>
)
