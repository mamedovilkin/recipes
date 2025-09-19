package com.ilkinmamedov.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorDto(
    val status: String,
    val code: Int,
    val message: String
)