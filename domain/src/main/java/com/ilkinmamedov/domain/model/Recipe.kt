package com.ilkinmamedov.domain.model

data class Recipe(
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