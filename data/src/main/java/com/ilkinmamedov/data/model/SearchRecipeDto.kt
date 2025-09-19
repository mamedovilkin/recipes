package com.ilkinmamedov.data.model

import com.ilkinmamedov.domain.model.SearchRecipe
import kotlinx.serialization.Serializable

@Serializable
data class SearchRecipeDto(
    val id: Long,
    val image: String,
    val imageType: String,
    val title: String,
)

fun SearchRecipeDto.toSearchRecipeDomain() : SearchRecipe {
    return SearchRecipe(
        id = id,
        image = image,
        imageType = imageType,
        title = title,
    )
}