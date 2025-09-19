package com.ilkinmamedov.data.repository

import com.ilkinmamedov.data.BuildConfig
import com.ilkinmamedov.domain.repository.RecipesResult
import com.ilkinmamedov.data.dao.RecipesDao
import com.ilkinmamedov.data.entity.toDomain
import com.ilkinmamedov.data.entity.toSearchRecipeDomain
import com.ilkinmamedov.data.model.ApiErrorDto
import com.ilkinmamedov.data.model.DetailRecipeDto
import com.ilkinmamedov.data.model.RecipesDto
import com.ilkinmamedov.data.model.SearchRecipeDto
import com.ilkinmamedov.data.model.SearchResultsDto
import com.ilkinmamedov.data.model.toDomain
import com.ilkinmamedov.data.model.toEntity
import com.ilkinmamedov.data.model.toSearchRecipeDomain
import com.ilkinmamedov.data.util.isInternetAvailable
import com.ilkinmamedov.domain.model.SearchRecipe
import com.ilkinmamedov.domain.repository.DetailRecipeResult
import com.ilkinmamedov.domain.repository.RecipesRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class RecipesRepositoryImpl(
    private val httpClient: HttpClient,
    private val recipesDao: RecipesDao
) : RecipesRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private val searchRecipes = MutableStateFlow<List<SearchRecipe>>(emptyList())

    override suspend fun getRandomRecipes(): Flow<RecipesResult> = flow {
        if (isInternetAvailable()) {
            val response = httpClient.get("random?number=20&apiKey=${BuildConfig.API_KEY}")
            val raw = response.bodyAsText()

            try {
                val dto = json.decodeFromString<RecipesDto>(raw)
                val entities = dto.recipes.map { it.toEntity() }
                recipesDao.insertAllRecipes(entities)

                emitAll(
                    recipesDao.getAllRecipes().map { entries ->
                        RecipesResult.Success(entries.map { it.toDomain() })
                    }
                )
            } catch (_: Exception) {
                val recipes = recipesDao.getAllRecipes().first()

                if (recipes.isEmpty()) {
                    val error = json.decodeFromString<ApiErrorDto>(raw)
                    emit(RecipesResult.Failure(error.code, error.message))
                } else {
                    emitAll(
                        recipesDao.getAllRecipes().map { entries ->
                            RecipesResult.Success(entries.map { it.toDomain() })
                        }
                    )
                }
            }
        } else {
            val recipes = recipesDao.getAllRecipes().first()

            if (recipes.isEmpty()) {
                emit(RecipesResult.Failure(-1, "No internet connection"))
            } else {
                emitAll(
                    recipesDao.getAllRecipes().map { entries ->
                        RecipesResult.Success(entries.map { it.toDomain() })
                    }
                )
            }
        }
    }

    override suspend fun getRecipeDetail(id: Long): Flow<DetailRecipeResult> = flow {
        if (isInternetAvailable()) {
            val response = httpClient.get("$id/information?apiKey=${BuildConfig.API_KEY}")
            val raw = response.bodyAsText()

            try {
                val dto = json.decodeFromString<DetailRecipeDto>(raw)
                emit(DetailRecipeResult.Success(dto.toDomain()))
            } catch (_: Exception) {
                val error = json.decodeFromString<ApiErrorDto>(raw)
                emit(DetailRecipeResult.Failure(error.code, error.message))
            }
        } else {
            emit(DetailRecipeResult.Failure(-1, "No internet connection"))
        }
    }

    override suspend fun searchRecipes(query: String): Flow<List<SearchRecipe>> = flow {
        if (isInternetAvailable()) {
            val response = httpClient.get("complexSearch?query=$query&apiKey=${BuildConfig.API_KEY}")
            val raw = response.bodyAsText()

            try {
                val dto = json.decodeFromString<SearchResultsDto>(raw)
                val recipes = dto.results.map { it.toSearchRecipeDomain() }

                searchRecipes.value += recipes

                emit(searchRecipes.value)
            } catch (_: Exception) {
                emitAll(
                    recipesDao.searchRecipes(query).map { entries ->
                        entries.map { it.toSearchRecipeDomain() }
                    }
                )
            }
        } else {
            emitAll(
                recipesDao.searchRecipes(query).map { entries ->
                    entries.map { it.toSearchRecipeDomain() }
                }
            )
        }
    }

    override suspend fun getSimilarRecipes(id: Long): Flow<List<SearchRecipe>?> = flow {
        if (isInternetAvailable()) {
            val response = httpClient.get("$id/similar?apiKey=${BuildConfig.API_KEY}")
            val raw = response.bodyAsText()

            try {
                val dto = json.decodeFromString<List<SearchRecipeDto>>(raw)
                val recipes = dto.map { it.toSearchRecipeDomain() }

                emit(recipes)
            } catch (_: Exception) {
                emit(null)
            }
        } else {
            emit(null)
        }
    }
}