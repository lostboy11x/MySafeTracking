package com.example.mysafetracking.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.mysafetracking.ui.theme.MySafeTrackingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MySafeTrackingTheme {
                val navController = rememberNavController()
                NavigationGraph(navController)
            }
        }

        // Només demanar permisos si encara no estan concedits
        if (!checkLocationPermissions()) {
            Log.d("MainActivity", "Requesting location permissions")
            requestLocationPermissions()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !checkBackgroundPermission()) {
            Log.d("MainActivity", "Requesting background location permission")
            requestBackgroundPermission()
        }
    }

    override fun onResume() {
        super.onResume()

        // Comprovar si els permisos han canviat quan l'usuari torna a l'aplicació
        if (checkLocationPermissions() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!checkBackgroundPermission()) {
                Log.d("MainActivity", "Requesting background location permission on resume")
                requestBackgroundPermission()
            }
        } else if (!checkLocationPermissions()) {
            requestLocationPermissions()
        }
    }

    /**
     * Funció per comprovar si els permisos de ubicació en primer pla (FINE i COARSE) estan concedits.
     */
    private fun checkLocationPermissions(): Boolean {
        val fineLocation =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocation =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fineLocation == PackageManager.PERMISSION_GRANTED && coarseLocation == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Funció per sol·licitar els permisos de ubicació en primer pla (FINE i COARSE).
     */
    private fun requestLocationPermissions() {
        requestPermissionsLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    /**
     * Funció per comprovar si el permís de ubicació en segon pla (BACKGROUND) està concedit.
     */
    private fun checkBackgroundPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Abans d'Android 10, el permís de fons no és necessari
            true
        }
    }

    /**
     * Funció per sol·licitar el permís de ubicació en segon pla (BACKGROUND) si ja tenim els altres permisos concedits.
     */
    private fun requestBackgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestBackgroundPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    // Launcher per gestionar la sol·licitud dels permisos de FINE i COARSE.
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        // Si es concedeixen FINE i COARSE, es demana el permís de segon pla
        if (fineGranted && coarseGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestBackgroundPermission()
        }
    }

    // 🔹 Launcher per gestionar la sol·licitud del permís de BACKGROUND.
    private val requestBackgroundPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Log.d("MainActivity", "Background location permission granted")
        } else {
            Log.d("MainActivity", "Background location permission denied")
            // ❌ Aquí podries mostrar un diàleg explicant per què és important concedir aquest permís.
        }
    }
}