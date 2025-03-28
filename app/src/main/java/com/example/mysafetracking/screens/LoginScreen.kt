package com.example.mysafetracking.screens

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.mysafetracking.data.db.viewmodels.TutorViewModel
import com.example.mysafetracking.data.setTutor
import com.example.mysafetracking.logic.validateLogin
import kotlinx.coroutines.launch

// Login
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, tutorViewModel: TutorViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    // Validar sol quan l'usuari faci click al botó
    /*
    fun handleLogin() {
        // Primer validem que el correu i la contrasenya no siguin buits
        errorMessage = validateLogin(email, password)
        isValid = errorMessage.isEmpty()

        if (isValid) {
            // Comprovar les credencials a la base de dades Room
            tutorViewModel.getTutor(email) { tutor ->
                if (tutor != null && tutor.password == password) { // Comprova que la contrasenya sigui correcta
                    isValid = true
                    errorMessage = ""
                    tutorViewModel.loadTutor(tutor) //
                    Log.d("TutorViewModel", "Tutor loaded: $tutor")

                    // Passar l'objecte tutor a la següent pantalla
                    navController.navigate("menuTutor") {
                        popUpTo("loginForm") { inclusive = true }
                        popUpTo("authorize") { inclusive = true }
                    }
                } else {
                    isValid = false
                    errorMessage = "Usuari o contrasenya incorrectes"
                }
            }
        }
    }*/
    val coroutineScope = rememberCoroutineScope()

    fun handleLogin() {
        errorMessage = validateLogin(email, password)
        isValid = errorMessage.isEmpty()

        if (isValid) {
            coroutineScope.launch {
                val tutor = tutorViewModel.getTutorSync(email)
                if (tutor != null && tutor.password == password) {
                    isValid = true
                    errorMessage = ""
                    setTutor(tutor)
                    navController.navigate("menuTutor") {
                        popUpTo("loginForm") { inclusive = true }
                        popUpTo("authorize") { inclusive = true }
                    }
                } else {
                    isValid = false
                    errorMessage = "Usuari o contrasenya incorrectes"
                }
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
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Títol
                Text(
                    text = "Iniciar Sessió",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Camp d'email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        //errorMessage = validateLogin(email, password)
                    },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = "Email")
                    },
                    isError = email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
                )

                // Mostrar missatge d'error si el login falla
                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                }

                // Botó d'iniciar sessió
                Button(
                    onClick = {
                        handleLogin()
                    },
                    enabled = email.isNotBlank() && password.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp, max = 56.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Iniciar Sessió", fontSize = 18.sp)
                }

                // Botó de registrar-se
                TextButton(
                    onClick = {
                        //navController.popBackStack()
                        navController.navigate("register") {
                            popUpTo("loginForm") { inclusive = true }
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Crear un Compte", fontSize = 16.sp)
                }
            }
        }
    }
}