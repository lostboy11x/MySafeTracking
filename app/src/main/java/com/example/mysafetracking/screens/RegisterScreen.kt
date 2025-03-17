package com.example.mysafetracking.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mysafetracking.R
import com.example.mysafetracking.logic.validateRegister

// Register
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Funció que maneja el registre
    fun handleRegister() {
        errorMessage = validateRegister(firstName, lastName, email, password)
        if (errorMessage.isEmpty()) {
            // Aquí va la lógica per a registrar l'usuari
            // Després de registrar-se, navegar a la GifScreen
            navController.navigate("gifScreen") {
                popUpTo("register") { inclusive = true }
                popUpTo("authorize") { inclusive = true }
            }
        }
    }

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
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Títol
                Text(
                    text = "Crear un Compte",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Camp nom
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("Nom") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Nom")
                    },
                    isError = firstName.isBlank(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = outlinedTextFieldColors(
                        focusedBorderColor = if (firstName.isNotBlank()) Color.Green else Color.Gray, // Verd si no està buit
                        unfocusedBorderColor = if (firstName.isNotBlank()) Color.Green else Color.Gray, // Verd si no està buit
                        errorBorderColor = Color.Red // Vermell si està buit
                    )
                )

                // Camp cognoms
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Cognom") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Cognom")
                    },
                    isError = lastName.isBlank(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = outlinedTextFieldColors(
                        focusedBorderColor = if (lastName.isNotBlank()) Color.Green else Color.Gray, // Verd si no està buit
                        unfocusedBorderColor = if (lastName.isNotBlank()) Color.Green else Color.Gray, // Verd si no està buit
                        errorBorderColor = Color.Red // Vermell si està buit
                    )
                )

                // Camp email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = "Email")
                    },
                    isError = email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = outlinedTextFieldColors(
                        focusedBorderColor = if (email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(
                                email
                            ).matches()
                        ) Color.Green else Color.Gray, // Verd quan l'email sigui vàlid
                        unfocusedBorderColor = if (email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(
                                email
                            ).matches()
                        ) Color.Green else Color.Gray, // Verd quan l'email sigui vàlid
                        errorBorderColor = Color.Red // Vermell en cas d'error
                    )
                )

                // Camp de contrasenya
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        //errorMessage = validateLogin(email, password)
                    },
                    label = { Text("Contrasenya") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Contrasenya")
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    //visualTransformation = PasswordVisualTransformation(),
                    isError = password.isNotBlank() && password.length < 8,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = outlinedTextFieldColors(
                        focusedBorderColor = if (password.isNotBlank() && password.length >= 8) Color.Green else Color.Gray, // Verd quan la contrasenya té almenys 8 caràcters
                        unfocusedBorderColor = if (password.isNotBlank() && password.length >= 8) Color.Green else Color.Gray, // Verd quan la contrasenya té almenys 8 caràcters
                        errorBorderColor = Color.Red // Vermell en cas d'error
                    )
                )

                // Mostrar missatge d'error si la validació falla
                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                }

                // Botó de registre
                Button(
                    onClick = {
                        handleRegister()
                    },
                    enabled = firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && password.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp, max = 56.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Registrar-se", fontSize = 18.sp)
                }

                // Botó de login
                TextButton(
                    onClick = {
                        //navController.popBackStack()
                        navController.navigate("loginForm") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Ja tinc un compte", fontSize = 16.sp)
                }
            }
        }
    }
}