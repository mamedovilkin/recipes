package com.ilkinmamedov.data.model

import com.ilkinmamedov.domain.model.DetailRecipe
import kotlinx.serialization.Serializable

@Serializable
data class DetailRecipeDto(
    val id: Long,
    val image: String? = null,
    val title: String? = null,
    val readyInMinutes: Int? = null,
    val servings: Int? = null,
    val vegetarian: Boolean? = null,
    val vegan: Boolean? = null,
    val glutenFree: Boolean? = null,
    val dairyFree: Boolean? = null,
    val veryHealthy: Boolean? = null,
    val cheap: Boolean? = null,
    val veryPopular: Boolean? = null,
    val preparationMinutes: Int? = null,
    val cookingMinutes: Int? = null,
    val aggregateLikes: Int? = null,
    val healthScore: Double? = null,
    val pricePerServing: Double? = null,
    val extendedIngredients: List<IngredientDto>? = null,
    val summary: String? = null,
    val dishTypes: List<String>? = null,
    val spoonacularSourceUrl: String? = null,
)

fun DetailRecipeDto.toDomain(): DetailRecipe {
    return DetailRecipe(
        id = id,
        image = image,
        title = title,
        readyInMinutes = readyInMinutes,
        servings = servings,
        vegetarian = vegetarian,
        vegan = vegan,
        glutenFree = glutenFree,
        dairyFree = dairyFree,
        veryHealthy = veryHealthy,
        cheap = cheap,
        veryPopular = veryPopular,
        preparationMinutes = preparationMinutes,
        cookingMinutes = cookingMinutes,
        aggregateLikes = aggregateLikes,
        healthScore = healthScore,
        pricePerServing = pricePerServing,
        extendedIngredients = extendedIngredients?.map { it.toDomain() },
        summary = summary,
        dishTypes = dishTypes,
        spoonacularSourceUrl = spoonacularSourceUrl,
    )
}