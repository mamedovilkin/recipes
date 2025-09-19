package com.ilkinmamedov.recipes.presentation.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilkinmamedov.domain.model.DetailRecipe
import com.ilkinmamedov.domain.model.SearchRecipe
import com.ilkinmamedov.domain.repository.DetailRecipeResult
import com.ilkinmamedov.domain.usecase.DetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DetailScreenState {
    data class Success(
        val detail: DetailRecipe,
        val similarRecipes: List<SearchRecipe>? = null
    ) : DetailScreenState
    data class Error(val message: String) : DetailScreenState
    object Loading : DetailScreenState
}

data class DetailUiState(
    val detailScreenState: DetailScreenState = DetailScreenState.Loading,
    val isExpandedSummary: Boolean = false,
)

class DetailViewModel(
    private val detailUseCase: DetailUseCase,
    private val id: Long,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        fetchDetail()
    }

    fun fetchDetail() = viewModelScope.launch {
        detailUseCase.getRecipeDetail(id).collect { result ->
            if (result is DetailRecipeResult.Failure) {
                _uiState.update { currentState ->
                    currentState.copy(
                        detailScreenState = DetailScreenState.Error(result.message)
                    )
                }
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        detailScreenState = DetailScreenState.Success((result as DetailRecipeResult.Success).detail)
                    )
                }
            }
        }

        detailUseCase.getSimilarRecipes(id).collect { recipes ->
            if (_uiState.value.detailScreenState is DetailScreenState.Success) {
                _uiState.update { currentState ->
                    currentState.copy(
                        detailScreenState = (currentState.detailScreenState as DetailScreenState.Success).copy(similarRecipes = recipes)
                    )
                }
            }
        }
    }

    fun toggleExpandedSummary() {
        _uiState.update { currentState ->
            currentState.copy(isExpandedSummary = !currentState.isExpandedSummary)
        }
    }
}