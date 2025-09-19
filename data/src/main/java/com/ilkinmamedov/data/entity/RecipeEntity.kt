package com.ilkinmamedov.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ilkinmamedov.domain.model.Recipe
import com.ilkinmamedov.domain.model.SearchRecipe

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val image: String,
    val imageType: String,
    val title: String,
    val readyInMinutes: Int,
    val servings: Int,
    val aggregateLikes: Int,
    val summary: String,
    val dishTypes: List<String>,
)

fun RecipeEntity.toDomain() : Recipe {
    return Recipe(
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

fun RecipeEntity.toSearchRecipeDomain() : SearchRecipe {
    return SearchRecipe(
        id = id,
        image = image,
        imageType = imageType,
        title = title
    )
}