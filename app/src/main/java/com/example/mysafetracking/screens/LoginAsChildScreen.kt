package com.example.mysafetracking.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mysafetracking.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAsChildScreen(navController: NavHostController) {
    var childCode by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Validar sol quan l'usuari faci click al botó
    /*
    fun handleLoginChild() {
        errorMessage = validateLogin(childCode, password)
        isValid = errorMessage.isEmpty()
        if (isValid) {
            navController.navigate("menuTutor") {
                popUpTo("loginForm") { inclusive = true }
                popUpTo("authorize") { inclusive = true }
            }
        }
    }
     */

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
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
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(180.dp))  // Afegeix espai a la part superior
                // Títol
                Text(
                    text = "Sóc un fill",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Camp codi pare-fill
                OutlinedTextField(
                    value = childCode,
                    onValueChange = {
                        childCode = it
                        //errorMessage = validateLogin(email, password)
                    },
                    label = { Text("Codi del fill") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "childCode")
                    },
                    isError = childCode.isNotBlank(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp),
                )

                // Mostrar missatge d'error si el login falla
                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                }

                // Botó d'iniciar sessió
                Button(
                    onClick = {
                        navController.navigate("menuChild") {
                            popUpTo("logincChild") { inclusive = true }
                            popUpTo("authorize") { inclusive = true }
                        }
                        //handleLoginChild()
                    },
                    enabled = childCode.isNotBlank(), //Todo: Afegir verificació que el codi és del pare
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp, max = 56.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Iniciar", fontSize = 18.sp)
                }
            }
        }
    }
}