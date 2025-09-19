package com.ilkinmamedov.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultsDto(
    val results: List<SearchRecipeDto>
)
