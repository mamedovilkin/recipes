package com.ilkinmamedov.domain.repository

import com.ilkinmamedov.domain.model.DetailRecipe

sealed class DetailRecipeResult {
    data class Success(val detail: DetailRecipe) : DetailRecipeResult()
    data class Failure(val code: Int, val message: String) : DetailRecipeResult()
}