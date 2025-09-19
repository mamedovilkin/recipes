package com.ilkinmamedov.data.model

import com.ilkinmamedov.domain.model.Ingredient
import kotlinx.serialization.Serializable

@Serializable
data class IngredientDto(
    val id: Long? = null,
    val name: String? = null,
    val image: String? = null,
    val original: String? = null,
)

fun IngredientDto.toDomain(): Ingredient {
    return Ingredient(
        id = id,
        name = name,
        image = image,
        original = original,
    )
}