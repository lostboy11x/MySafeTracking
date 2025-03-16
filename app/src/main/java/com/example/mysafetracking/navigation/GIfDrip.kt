package com.example.mysafetracking.navigation

import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mysafetracking.ui.theme.MySafeTrackingTheme
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlin.random.Random

@Composable
fun GifDrip(navController: NavHostController) {
    val context = LocalContext.current
    // Llista amb les 3 URLs dels GIFs
    val gifList = listOf(
        "https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExcHhyMWFvbnYyZng0OHN0MnhwbDFvODc3cmZ2emswZDM3d2h6YzZncCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/P6TDJvI3NB2Kq9MbBI/giphy.gif",
        "https://media1.tenor.com/m/J4JmyQx91dYAAAAd/eren-eren-yeager.gif",
        "https://media4.giphy.com/media/v1.Y2lkPTc5MGI3NjExcTE1OGsxbWQwN3RnZDludng0cngydTcyaWs4YXJ0aTl6ZTdoZGlyZCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/4g8fetI7jTtwO52QSy/giphy.gif"
    )

    // Estat per gestionar el URL del GIF actual
    var gifUrl by remember { mutableStateOf(gifList[0]) }

    MySafeTrackingTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            GifSwitcherView(
                gifUrl = gifUrl,
                onGifChange = { newUrl -> gifUrl = newUrl },
                modifier = Modifier.padding(innerPadding),
                context = context,
                gifList = gifList
            )
        }
    }
}

@Composable
fun GifSwitcherView(
    gifUrl: String,
    onGifChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    context: android.content.Context,
    gifList: List<String>
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mostrar el text de benvinguda i el nom de l'aplicació
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Not Implemented Yeat", // El text que vols posar
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontSize = 24.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Utilitzar Crossfade per mostrar el GIF amb animació
        Crossfade(
            targetState = gifUrl,
            animationSpec = tween(durationMillis = 700)
        ) { currentGifUrl ->
            // Aplicar animació de "scale"
            Box(
                modifier = Modifier.graphicsLayer(
                    scaleX = 1.1f,
                    scaleY = 1.1f,
                    alpha = 0.9f
                )
            ) {
                // Mostrar el GIF amb Glide dins un AndroidView
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    factory = { ctx ->
                        ImageView(ctx).apply {
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            adjustViewBounds = true
                        }
                    },
                    update = { imageView ->
                        Glide.with(context)
                            .asGif()
                            .load(currentGifUrl)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(imageView)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botó per canviar el GIF
        Button(onClick = {
            // Canviar el GIF aleatòriament de la llista
            onGifChange(gifList[Random.nextInt(gifList.size)])
        }) {
            Text(text = "You Got Dat Drip?")
        }
    }
}
