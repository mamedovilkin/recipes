package com.ilkinmamedov.recipes.presentation.screen.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilkinmamedov.recipes.R
import com.ilkinmamedov.recipes.presentation.composables.BackButton

@Composable
fun ErrorScreenState(
    message: String,
    onBack: (() -> Unit)? = null,
    onRetry: (() -> Unit)
) {
    onBack?.let {
        BackButton(
            onBack = it,
            modifier = Modifier.statusBarsPadding()
        )
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.error),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )
        Text(
            text = stringResource(R.string.something_went_wrong),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = message,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}