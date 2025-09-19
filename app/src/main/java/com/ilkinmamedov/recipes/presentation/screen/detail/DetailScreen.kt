package com.ilkinmamedov.recipes.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilkinmamedov.recipes.presentation.composables.DetailIngredients
import com.ilkinmamedov.recipes.presentation.composables.DetailRecipeTopHeader
import com.ilkinmamedov.recipes.presentation.composables.DetailSimilarRecipes
import com.ilkinmamedov.recipes.presentation.composables.DetailStatisticsGridCards
import com.ilkinmamedov.recipes.presentation.composables.DetailSummary
import com.ilkinmamedov.recipes.presentation.composables.RecipesCategoriesChips
import com.ilkinmamedov.recipes.presentation.screen.state.ErrorScreenState
import com.ilkinmamedov.recipes.presentation.screen.state.LoadingScreenState

@Composable
fun DetailScreen(
    onBack: () -> Unit,
    onOpenOnWebsite: (String?) -> Unit,
    onOpenDetail: (Long) -> Unit,
    detailViewModel: DetailViewModel
) {
    val uiState by detailViewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (val detailScreenState = uiState.detailScreenState) {
            is DetailScreenState.Loading -> {
                LoadingScreenState()
            }
            is DetailScreenState.Success -> {
                DetailScreenContent(
                    detailScreenState = detailScreenState,
                    onBack = onBack,
                    onOpenOnWebsite = onOpenOnWebsite,
                    onOpenDetail = onOpenDetail,
                    isExpandedSummary = uiState.isExpandedSummary,
                    onToggleExpandedSummary = { detailViewModel.toggleExpandedSummary() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
            is DetailScreenState.Error -> {
                ErrorScreenState(
                    message = detailScreenState.message,
                    onBack = onBack,
                    onRetry = {
                        detailViewModel.fetchDetail()
                    }
                )
            }
        }
    }
}

@Composable
fun DetailScreenContent(
    modifier: Modifier = Modifier,
    detailScreenState: DetailScreenState.Success,
    onBack: () -> Unit,
    onOpenOnWebsite: (String?) -> Unit,
    onOpenDetail: (Long) -> Unit,
    isExpandedSummary: Boolean,
    onToggleExpandedSummary: () -> Unit
) {
    val detail = detailScreenState.detail

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        item {
            DetailRecipeTopHeader(
                detail = detail,
                onBack = onBack,
                onOpenOnWebsite = onOpenOnWebsite,
            )
        }

        item {
            detail.title?.let {
                Text(
                    text = it,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        item {
            detail.dishTypes?.let {
                RecipesCategoriesChips(
                    categories = it,
                    selectedCategory = "",
                    onCategorySelected = {}
                )
            }
        }

        item {
            DetailStatisticsGridCards(detail = detail)
        }

        item {
            DetailSummary(
                detail = detail,
                isExpandedSummary = isExpandedSummary,
                onToggleExpandedSummary = onToggleExpandedSummary
            )
        }

        item {
            DetailIngredients(detail = detail)
        }

        item {
            DetailSimilarRecipes(
                detailScreenState = detailScreenState,
                onOpenDetail = onOpenDetail
            )
        }
    }
}