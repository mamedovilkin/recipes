package com.ilkinmamedov.recipes.presentation.composables

import android.widget.TextView
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.ilkinmamedov.domain.model.DetailRecipe
import com.ilkinmamedov.domain.model.getStatistics
import com.ilkinmamedov.recipes.R
import com.ilkinmamedov.recipes.presentation.screen.detail.DetailScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun RecipesTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = stringResource(R.string.app_name),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    )
}

@Composable
fun RecipesSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.primary
                )
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.weight(1F)
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search),
                            color = Color.Gray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    innerTextField()
                }
                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.clickable { onClearQuery() }
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions {
            keyboardController?.hide()
        },
        singleLine = true,
        textStyle = TextStyle(
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    )
}

@Composable
fun RecipesCategoriesChips(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit = {}
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            Text(
                text = category,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (selectedCategory == category) Color.White else Color.Gray,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (selectedCategory == category) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .clickable {
                        onCategorySelected(category)
                    }
                    .padding(horizontal = 16.dp, vertical = 4.dp)

            )
        }
    }
}

@Composable
fun RecipeCardStat(
    icon: Int,
    text: String,
    color: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun RecipeCard(
    modifier: Modifier = Modifier,
    image: String,
    title: String,
    summary: String? = null,
    servings: Int? = null,
    readyInMinutes: Int? = null,
    aggregateLikes: Int? = null,
    onOpenDetail: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onOpenDetail() }
    ) {
        Column {
            AsyncImage(
                model = image,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (title.length > 50) title.take(50) + "..." else title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (summary != null && servings != null && readyInMinutes != null && aggregateLikes != null) {
                AndroidView(
                    factory = {
                        TextView(it).apply {
                            textSize = 14.0F
                            setTextColor(it.getColor(R.color.gray))
                        }
                    },
                    update = {
                        it.text = HtmlCompat.fromHtml(if (summary.length > 250) summary.take(250) + "..." else summary, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    RecipeCardStat(
                        icon = R.drawable.ic_person,
                        text = servings.toString(),
                        color = Color(33, 150, 243)
                    )
                    RecipeCardStat(
                        icon = R.drawable.ic_time,
                        text = readyInMinutes.toString(),
                        color = Color.Gray
                    )
                    RecipeCardStat(
                        icon = R.drawable.ic_like,
                        text = aggregateLikes.toString(),
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun UpFloatingActionButton(
    onUpClick: () -> Unit,
) {
    SmallFloatingActionButton(
        onClick = onUpClick,
        shape = CircleShape,
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = null
        )
    }
}

@Composable
fun BackButton(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onBack,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_back),
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.background.copy(0.5F))
                .padding(4.dp)
        )
    }
}

@Composable
fun DetailRecipeTopHeader(
    detail: DetailRecipe,
    onBack: () -> Unit,
    onOpenOnWebsite: (String?) -> Unit,
) {
    Box {
        AsyncImage(
            model = detail.image,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color.Gray)
        )
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            BackButton(onBack = onBack)
            Spacer(modifier = Modifier.weight(1F))
            Button(
                onClick = { onOpenOnWebsite(detail.spoonacularSourceUrl) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(0.5F)
                )
            ) {
                Text(
                    text = stringResource(R.string.open_on_website),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun DetailStatisticsGridCards(
    detail: DetailRecipe
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.height(120.dp)
    ) {
        items(detail.getStatistics()) { stat ->
            stat?.let {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(120.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = it.value,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 26.sp,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it.label,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailSummary(
    detail: DetailRecipe,
    isExpandedSummary: Boolean,
    onToggleExpandedSummary: () -> Unit
) {
    detail.summary?.let { summary ->
        Text(
            text = stringResource(R.string.summary),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        AndroidView(
            factory = {
                TextView(it).apply {
                    textSize = 18.0F
                    setTextColor(ContextCompat.getColor(it, R.color.gray))
                }
            },
            update = {
                it.text = HtmlCompat.fromHtml(if (summary.length > 250 && isExpandedSummary) summary else summary.take(250) + "...", HtmlCompat.FROM_HTML_MODE_LEGACY)
            },
            modifier = Modifier
                .animateContentSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .clickable { onToggleExpandedSummary() }
        )
    }
}

@Composable
fun DetailIngredients(
    detail: DetailRecipe,
) {
    detail.extendedIngredients?.let { ingredients ->
        Text(
            text = stringResource(R.string.ingredients),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        ingredients.forEach { ingredient ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
            ) {
                ingredient.image?.let {
                    AsyncImage(
                        model = "https://img.spoonacular.com/ingredients_250x250/$it",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                }
                Column {
                    ingredient.name?.let {
                        Text(
                            it.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(java.util.Locale.getDefault()) else it },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    ingredient.original?.let {
                        Text(
                            it,
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailSimilarRecipes(
    detailScreenState: DetailScreenState.Success,
    onOpenDetail: (Long) -> Unit
) {
    detailScreenState.similarRecipes?.let { recipes ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.similar_recipes),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            LazyRow {
                items(recipes) { recipe ->
                    RecipeCard(
                        title = if (recipe.title.length > 25) recipe.title.take(25) + "..." else recipe.title,
                        image = "https://img.spoonacular.com/recipes/${recipe.id}-556x370.${recipe.imageType}",
                        onOpenDetail = { onOpenDetail(recipe.id) }
                    )
                }
            }
        }
    }
}