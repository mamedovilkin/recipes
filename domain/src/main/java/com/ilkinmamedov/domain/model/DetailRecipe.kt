package com.ilkinmamedov.domain.model

data class DetailRecipe(
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
    val extendedIngredients: List<Ingredient>? = null,
    val summary: String? = null,
    val dishTypes: List<String>? = null,
    val spoonacularSourceUrl: String? = null,
)

data class RecipeStat(val label: String, val value: String)

fun DetailRecipe.getStatistics(): List<RecipeStat?> = listOf(
    readyInMinutes?.let { RecipeStat("Ready (in minutes)", it.toString()) },
    servings?.let { RecipeStat("Servings", it.toString()) },
    vegetarian?.let { RecipeStat("Vegetarian", if (it) "Yes" else "No") },
    vegan?.let { RecipeStat("Vegan", if (it) "Yes" else "No") },
    glutenFree?.let { RecipeStat("Gluten free", if (it) "Yes" else "No") },
    dairyFree?.let { RecipeStat("Dairy free", if (it) "Yes" else "No") },
    veryHealthy?.let { RecipeStat("Very healthy", if (it) "Yes" else "No") },
    cheap?.let { RecipeStat("Cheap", if (it) "Yes" else "No") },
    veryPopular?.let { RecipeStat("Very popular", if (it) "Yes" else "No") },
    preparationMinutes?.let { RecipeStat("Preparation minutes", it.toString()) },
    cookingMinutes?.let { RecipeStat("Cooking minutes", it.toString()) },
    aggregateLikes?.let { RecipeStat("Aggregate likes", it.toString()) },
    healthScore?.let { RecipeStat("Health score", it.toString()) },
    pricePerServing?.let { RecipeStat("Price per serving", "$it$") }
)
