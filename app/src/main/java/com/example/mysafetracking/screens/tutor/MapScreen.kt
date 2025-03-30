package com.example.mysafetracking.screens.tutor

import android.Manifest
import com.google.android.gms.location.LocationServices
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.mysafetracking.R
import com.example.mysafetracking.data.Child
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import androidx.core.graphics.scale
import com.example.mysafetracking.data.getChildren
import com.google.android.gms.maps.CameraUpdateFactory
import android.content.pm.PackageManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import com.example.mysafetracking.ui.theme.DarkGrayText
import com.example.mysafetracking.ui.theme.TopGradientEnd
import com.example.mysafetracking.ui.theme.TopGradientStart


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun MapScreen(navController: NavHostController) {
    val children: List<Child> = getChildren().toMutableList()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedChildLocation by remember { mutableStateOf<LatLng?>(null) } // Ubicació seleccionada
    val cameraPositionState = rememberCameraPositionState()

    val permissionFineState = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val permissionCoarseState = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    Log.d("PERMISSIONS", "Fine Location: ${permissionFineState == PackageManager.PERMISSION_GRANTED}")
    Log.d("PERMISSIONS", "Coarse Location: ${permissionCoarseState == PackageManager.PERMISSION_GRANTED}")

    // Actualitzar la ubicació cada 5 segons
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000) // Espera 5 segons
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    userLocation = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(TopGradientStart, TopGradientEnd)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Secció del mapa (al mig)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(5.dp, Color.White)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(zoomControlsEnabled = false),
                    cameraPositionState = cameraPositionState,
                ) {
                    // Marker de la ubicació de l'usuari
                    userLocation?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = "Estàs aquí",
                            snippet = "Ubicació actual"
                        )
                    }

                    // Afegir markers per a cada nen
                    children.forEach { child ->
                        val childLocation = child.currentLocation
                        val position = childLocation?.let { LatLng(it.latitude, childLocation.longitude) }

                        position?.let { MarkerState(position = it) }?.let {
                            Marker(
                                state = it,
                                title = "${child.name} ${child.surname}",
                                icon = BitmapDescriptorFactory.fromBitmap(
                                    createMarkerIcon(child.photoProfile) // Funció que crea la imatge de perfil
                                )
                            )
                        }
                    }
                    // Mostrar el marker del nen seleccionat
                    selectedChildLocation?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = "Ubicació seleccionada",
                            snippet = "Aquest és el nen seleccionat"
                        )
                    }

                    // Actualitzar el zoom quan un nen és seleccionat
                    selectedChildLocation?.let {
                        LaunchedEffect(it) {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(it, 15f) // Aplicar el zoom a la ubicació seleccionada
                            )
                        }
                    }
                }
            }

            // Secció inferior (Box buit)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 16.dp), // Mateix padding horitzontal que el mapa
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White), // Fons blanc
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                LazyColumn {
                    items(children) { child -> // Iterar sobre la llista de nens
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    // Actualitzar la ubicació seleccionada en clicar sobre un nen
                                    val childLocation = child.currentLocation
                                    val position = childLocation?.let { LatLng(it.latitude, it.longitude) }
                                    selectedChildLocation = position
                                },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)) // Blau clar suau
                        ) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                Image(
                                    painter = painterResource(id = getImageResource(child.photoProfile)),
                                    contentDescription = "Foto de perfil del nen",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .border(1.dp, Color.Gray, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(text = "${child.name} ${child.surname}", fontWeight = FontWeight.Bold)
                                }
                            }

                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(50)),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Tornar enrere",
                    tint = DarkGrayText
                )
            }
        }
    }
}

// Funció per crear la icona del marker a partir de la foto de perfil
@SuppressLint("DiscouragedApi")
@Composable
fun createMarkerIcon(photoName: String): Bitmap {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(photoName, "drawable", context.packageName)
    val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
    return bitmap.scale(100, 100, false) // Escala la imatge per ajustar la mida del marker
}