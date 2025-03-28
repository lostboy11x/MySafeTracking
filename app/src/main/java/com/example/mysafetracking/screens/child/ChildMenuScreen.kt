package com.example.mysafetracking.screens.child

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mysafetracking.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import android.provider.Settings
import androidx.compose.material3.*
import androidx.core.net.toUri
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun ChildMenuScreen(navController: NavHostController) {
    val cameraPositionState = rememberCameraPositionState()
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Variable per controlar la visibilitat del diàleg
    var permissionGranted by remember { mutableStateOf(false) }

    // Mostrar un diàleg per sol·licitar l'accés a la configuració de localització en segon pla
    var showPermissionDialog by remember { mutableStateOf(false) }
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Permís de localització en segon pla") },
            text = {
                Text("Per a obtenir la teva ubicació mentre l'aplicació està en segon pla, necessitem que permetis l'accés a la localització en segon pla. Vols anar a la configuració per activar aquest permís?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Redirigeix a la configuració de permisos
                        showPermissionDialog = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = "package:${context.packageName}".toUri()
                        context.startActivity(intent)
                    }
                ) {
                    Text("Acceptar")
                }
            }
        )
    }

    // Llançador de permisos
    val backgroundLocationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted = granted
        if (granted) {
            Toast.makeText(context, "Permís de localització en segon pla concedit", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permís de localització en segon pla denegat", Toast.LENGTH_SHORT).show()
            showPermissionDialog = true
        }
    }

    // Repetir la sol·licitud fins que es concedeixi el permís
    LaunchedEffect(permissionGranted) {
        if (!permissionGranted) {
            showPermissionDialog = true
        } else {
            showPermissionDialog = false
        }
    }


    // Comprovar permisos de localització en segon pla
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when {
                context.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                    // Si ja tenim el permís, comencem a obtenir la localització
                    startLocationUpdates(fusedLocationClient, cameraPositionState)
                }
                else -> {
                    // Sol·licitem el permís si no el tenim
                    backgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

                }
            }
        } else {
            // Si la versió és anterior a Android Q, no es necessita aquest permís
            startLocationUpdates(fusedLocationClient, cameraPositionState)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    userLocation = LatLng(location.latitude, location.longitude)
                }
            }
            delay(30000)
        }
    }
    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 15f)
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // Secció superior (nom de l'app)
                    Text(
                        text = stringResource(R.string.app_name),
                        color = Color.White,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Tornar enrere",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background) // Color de fons
        ) {
            // Secció del mapa (al mig)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(16.dp) // Padding al voltant del mapa
                    .background(
                        Color(0xFFF1F1F1), // Color de fons lleuger per al mapa
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(zoomControlsEnabled = false),
                    cameraPositionState = cameraPositionState

                ) {
                    // Marker de la ubicació de l'usuari
                    userLocation?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = "Estàs aquí",
                            snippet = "Ubicació actual"
                        )
                    }

                }
            }
        }
    }

}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
private fun startLocationUpdates(
    fusedLocationClient: FusedLocationProviderClient,
    cameraPositionState: CameraPositionState
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            cameraPositionState.move(CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)))
        }
    }
}