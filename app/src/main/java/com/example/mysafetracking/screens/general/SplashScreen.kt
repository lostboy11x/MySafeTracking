package com.example.mysafetracking.screens.general

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mysafetracking.R
import com.example.mysafetracking.ui.theme.TopGradientEnd
import com.example.mysafetracking.ui.theme.TopGradientStart
import kotlinx.coroutines.delay


@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(5000) // Espera 5 segons abans de navegar
        onTimeout()
    }

    val letters = stringResource(R.string.app_name)
    val positions = remember { letters.map { Animatable((0..300).random().toFloat()) } }
    val directions = remember { letters.map { (0..3).random() } }

    letters.forEachIndexed { index, _ ->
        LaunchedEffect(Unit) {
            positions[index].animateTo(0f, animationSpec = tween(2000))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(TopGradientStart, TopGradientEnd)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Row {
            letters.forEachIndexed { index, letter ->
                Text(
                    text = letter.toString(),
                    fontSize = 32.sp,
                    color = Color.White,
                    modifier = Modifier.offset(
                        x = if (directions[index] % 2 == 0) positions[index].value.dp else 0.dp,
                        y = if (directions[index] % 2 == 1) positions[index].value.dp else 0.dp
                    )
                )
            }
        }
    }
}