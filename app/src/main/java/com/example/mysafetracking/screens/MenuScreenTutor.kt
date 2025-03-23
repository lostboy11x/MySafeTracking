package com.example.mysafetracking.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mysafetracking.R
import com.example.mysafetracking.data.Child
import com.example.mysafetracking.data.Location
import com.example.mysafetracking.data.drawableImages
import com.example.mysafetracking.data.getChildren

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreenTutor(navController: NavHostController) {
    var children = getChildren()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = Color.White,
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botó per anar a la pantalla del mapa
            Button(
                onClick = {
                    navController.navigate("mapScreen") {
                        popUpTo("menuTutor")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Veure el mapa", fontWeight = FontWeight.Bold)
            }

            // Llista de nens
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(children) { child ->
                    ChildItem(child, navController)
                }
            }
        }
    }
}

// Element individual de la llista de nens
@Composable
fun ChildItem(child: Child, navController: NavHostController) {
    val imageResource = getImageResource(child.photoProfile)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
            /* Aquí podries afegir una acció, com veure més detalls */
                navController.navigate("gifScreen") {
                    popUpTo("menuTutor") { inclusive = true }
                }
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)) // Blau clar suau
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "Foto de perfil del nen",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "${child.name} ${child.surname} ${child.childCode}", fontWeight = FontWeight.Bold)
                child.currentLocation?.let {
                    Text(
                        text = "Ubicació: Lat ${it.latitude}, Lng ${it.longitude}",
                        color = Color.Gray
                    )
                } ?: Text(text = "Ubicació no disponible", color = Color.Gray)
            }
        }

    }
}


// Funció per obtenir la imatge del recurs drawable en base al nom
@Composable
fun getImageResource(imageName: String): Int {
    val context = LocalContext.current
    // Obtenim el recurs de drawable a partir del nom de la imatge
    val resId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
    return if (resId != 0) resId else R.drawable.gee_me_002 // Retornem la imatge per defecte si no existeix
}

