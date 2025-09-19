package com.ilkinmamedov.recipes.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilkinmamedov.domain.model.Recipe
import com.ilkinmamedov.domain.model.SearchRecipe
import com.ilkinmamedov.domain.repository.RecipesResult
import com.ilkinmamedov.domain.usecase.HomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeScreenState {
    data class Success(val recipes: List<Recipe>) : HomeScreenState
    data class Error(val message: String) : HomeScreenState
    object Loading : HomeScreenState
}

data class HomeUiState(
    val query: String = "",
    val searchResults: List<SearchRecipe> = emptyList(),
    val categories: Set<String> = emptySet(),
    val selectedCategory: String = "",
    val homeScreenState: HomeScreenState = HomeScreenState.Loading
)

class HomeViewModel(
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchRecipes()
    }

    fun fetchRecipes() = viewModelScope.launch {
        homeUseCase.getAllRecipes().collect { result ->
            if (result is RecipesResult.Failure) {
                _uiState.update { currentState ->
                    currentState.copy(
                        homeScreenState = HomeScreenState.Error(result.message)
                    )
                }
            } else {
                _uiState.update { currentState ->
                    val recipes = (result as RecipesResult.Success).recipes
                    currentState.copy(
                        homeScreenState = if (currentState.selectedCategory.isEmpty()){
                            HomeScreenState.Success(recipes)
                        } else {
                            HomeScreenState.Success(recipes.filter { it.dishTypes.contains(currentState.selectedCategory) })
                        },
                        categories = recipes.flatMap { it.dishTypes }.toSet()
                    )
                }
            }
        }
    }

    fun searchRecipes(query: String) = viewModelScope.launch {
        homeUseCase.searchRecipes(query).collect { results ->
            _uiState.update { currentState ->
                currentState.copy(
                    searchResults = results
                )
            }
        }
    }

    fun setSearch(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                query = query,
                selectedCategory = ""
            )
        }

        if (query.isEmpty()) {
            fetchRecipes()
        } else {
            searchRecipes(query)
        }
    }

    fun setSelectedCategory(category: String) = viewModelScope.launch {
        _uiState.update { currentState ->
            currentState.copy(selectedCategory = if (currentState.selectedCategory == category) "" else category)
        }

        fetchRecipes()
    }
}