package com.ilkinmamedov.recipes.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilkinmamedov.domain.model.Recipe
import com.ilkinmamedov.domain.model.SearchRecipe
import com.ilkinmamedov.recipes.R
import com.ilkinmamedov.recipes.presentation.composables.RecipeCard
import com.ilkinmamedov.recipes.presentation.composables.RecipesCategoriesChips
import com.ilkinmamedov.recipes.presentation.composables.RecipesSearchBar
import com.ilkinmamedov.recipes.presentation.composables.RecipesTopBar
import com.ilkinmamedov.recipes.presentation.composables.UpFloatingActionButton
import com.ilkinmamedov.recipes.presentation.screen.state.ErrorScreenState
import com.ilkinmamedov.recipes.presentation.screen.state.LoadingScreenState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
@Preview
fun HomeScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
    onOpenDetail: (Long) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val uiState by homeViewModel.uiState.collectAsState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisible = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            lastVisible != null && lastVisible >= (uiState.homeScreenState as HomeScreenState.Success).recipes.size - 1
        }
    }

    val showUpFloatingActionButton by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && uiState.query.isNotEmpty()) {
            homeViewModel.searchRecipes(uiState.query)
        } else if (shouldLoadMore.value) {
            homeViewModel.fetchRecipes()
        }
    }

    Scaffold(
        topBar = { RecipesTopBar() },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when (val homeScreenState = uiState.homeScreenState) {
            is HomeScreenState.Loading -> {
                LoadingScreenState()
            }
            is HomeScreenState.Success -> {
                HomeScreenContent(
                    query = uiState.query,
                    onQueryChange = { homeViewModel.setSearch(it) },
                    onClearQuery = { homeViewModel.setSearch("") },
                    lazyListState = lazyListState,
                    categories = uiState.categories.toList(),
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = { homeViewModel.setSelectedCategory(it) },
                    searchResults = uiState.searchResults,
                    recipes = homeScreenState.recipes,
                    showUpFloatingActionButton = showUpFloatingActionButton,
                    onUpClick = {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(index = 0)
                        }
                    },
                    onOpenDetail = onOpenDetail,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is HomeScreenState.Error -> {
                ErrorScreenState(
                    message = homeScreenState.message,
                    onRetry = {
                        homeViewModel.fetchRecipes()
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    lazyListState: LazyListState,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    searchResults: List<SearchRecipe>,
    recipes: List<Recipe>,
    showUpFloatingActionButton: Boolean,
    onUpClick: () -> Unit,
    onOpenDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RecipesSearchBar(
                query = query,
                onQueryChange = onQueryChange,
                onClearQuery = onClearQuery
            )
            Box(modifier = Modifier.fillMaxSize()) {
                Column{
                    LazyColumn(
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                AnimatedVisibility(query.isEmpty()) {
                                    RecipesCategoriesChips(
                                        categories = categories.toList(),
                                        selectedCategory = selectedCategory,
                                        onCategorySelected = onCategorySelected
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                ) {
                                    AnimatedVisibility(query.isEmpty()) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_popular_recipes),
                                            contentDescription = null,
                                            tint = Color.Red,
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }
                                    Text(
                                        text = if (query.isNotEmpty()) if (searchResults.isNotEmpty()) stringResource(R.string.results_for, query) else stringResource(R.string.no_results_for, query) else stringResource(R.string.popular_recipes),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 24.sp
                                    )
                                }
                            }
                        }

                        if (query.isNotEmpty()) {
                            items(searchResults) { recipe ->
                                RecipeCard(
                                    modifier = Modifier.animateItem(),
                                    title = recipe.title,
                                    image = recipe.image,
                                    onOpenDetail = { onOpenDetail(recipe.id) }
                                )
                            }
                        } else {
                            items(recipes) { recipe ->
                                RecipeCard(
                                    modifier = Modifier.animateItem(),
                                    title = recipe.title,
                                    image = recipe.image,
                                    summary = recipe.summary,
                                    servings = recipe.servings,
                                    readyInMinutes = recipe.readyInMinutes,
                                    aggregateLikes = recipe.aggregateLikes,
                                    onOpenDetail = { onOpenDetail(recipe.id) }
                                )
                            }
                        }

                    }
                }
                Column(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    AnimatedVisibility(
                        showUpFloatingActionButton,
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it }
                    ) {
                        UpFloatingActionButton(onUpClick = onUpClick)
                    }
                }
            }
        }
    }
}