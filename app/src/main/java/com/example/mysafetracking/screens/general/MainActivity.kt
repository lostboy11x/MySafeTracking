package com.example.mysafetracking.screens.general

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.mysafetracking.data.db.database.AppDatabase
import com.example.mysafetracking.data.db.repository.TutorRepository
import com.example.mysafetracking.data.db.viewmodels.TutorViewModel
import com.example.mysafetracking.ui.theme.MySafeTrackingTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient

class MainActivity : ComponentActivity() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mSettingsClient: SettingsClient
    private val _showSnackbar = mutableStateOf<String?>(null)
    private val _showSettingsDialog = mutableStateOf(false)

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<Array<String>>
    private var settings = false
    private val _requestingLocationUpdates = mutableStateOf(false)

    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationSettingsRequest: LocationSettingsRequest

    private val sharedPreferences by lazy { getSharedPreferences("permissions", MODE_PRIVATE) }
    private var permissionsGranted: Boolean
        get() = sharedPreferences.getBoolean("permissions_granted", false)
        set(value) {
            sharedPreferences.edit() { putBoolean("permissions_granted", value) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Log.i("TAG", "Precise location access granted.")
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.i("TAG", "Coarse location access granted.")
                }

                else -> {
                    _showSnackbar.value = "Location permission denied."
                }
            }
        }

        setContent {
            MySafeTrackingTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val database = remember { AppDatabase.getDatabase(context) }
                val tutorRepository =
                    remember { TutorRepository(database.tutorDao(), database.childDao()) }
                val tutorViewModel = remember { TutorViewModel(tutorRepository) }

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                mSettingsClient = LocationServices.getSettingsClient(this)

                NavigationGraph(navController = navController, tutorViewModel = tutorViewModel)

                SettingsDialog(
                    showDialog = _showSettingsDialog.value,
                    onDismiss = {
                        _showSettingsDialog.value = false
                        _requestingLocationUpdates.value = false
                    },
                    onOpenSettings = {
                        openLocationSettings()
                        _showSettingsDialog.value = false
                    }
                )

                _showSnackbar.value?.let { message ->
                    val snackbarHostState = remember { SnackbarHostState() }

                    LaunchedEffect(message) {
                        snackbarHostState.showSnackbar(message)
                        _showSnackbar.value = null
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }

    private fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        try {
            if (!checkPermissions() && !settings && !permissionsGranted) {
                requestPermissions()
            } else if (!checkPermissions() && settings) {
                _showSnackbar.value = "Location is unavailable. You need to enable the permissions."
            }

            mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this) {
                    Log.i("TAG", "All location settings are satisfied.")
                    //startLocationUpdates()
                }
                .addOnFailureListener(this) { e ->
                    Log.e("TAG", "Location settings are not satisfied.")
                    _showSettingsDialog.value = true
                }

        } catch (e: Exception) {
            Log.e("TAG", "Error in onResume: ${e.message}")
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionFineState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val permissionCoarseState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        return ((permissionFineState == PackageManager.PERMISSION_GRANTED) ||
                (permissionCoarseState == PackageManager.PERMISSION_GRANTED))
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) || ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (shouldProvideRationale) {
            _showSettingsDialog.value = true
            settings = true
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

    }
}

@Composable
fun SettingsDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Es necessiten els serveis de localització") },
            text = { Text("No es pot activar la localització des de l'aplicació. Vols anar a la configuració manualment?") },
            confirmButton = {
                TextButton(onClick = onOpenSettings) {
                    Text("Anar a Configuració")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel·lar")
                }
            }
        )
    }
}
