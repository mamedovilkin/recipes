package com.ilkinmamedov.data.model

import com.ilkinmamedov.data.entity.RecipeEntity
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val id: Long,
    val image: String,
    val imageType: String,
    val title: String,
    val readyInMinutes: Int,
    val servings: Int,
    val aggregateLikes: Int,
    val summary: String,
    val dishTypes: List<String>,
)

fun RecipeDto.toEntity() : RecipeEntity {
    return RecipeEntity(
        id = id,
        image = image,
        imageType = imageType,
        title = title,
        readyInMinutes = readyInMinutes,
        servings = servings,
        aggregateLikes = aggregateLikes,
        summary = summary,
        dishTypes = dishTypes
    )
}