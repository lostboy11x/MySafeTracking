package com.example.mysafetracking.screens.general

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mysafetracking.data.db.entities.TutorEntity
import com.example.mysafetracking.data.db.viewmodels.TutorViewModel
import com.example.mysafetracking.logic.validateRegister
import com.example.mysafetracking.ui.theme.ButtonGradientEnd
import com.example.mysafetracking.ui.theme.ButtonGradientStart
import com.example.mysafetracking.ui.theme.DarkGrayText
import com.example.mysafetracking.ui.theme.HintGrayText
import com.example.mysafetracking.ui.theme.LightGray
import com.example.mysafetracking.ui.theme.TopGradientEnd
import com.example.mysafetracking.ui.theme.TopGradientStart
import java.util.UUID


// Register
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController, tutorViewModel: TutorViewModel) {
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
            // Generar un ID únic per al tutor
            val tutorId = UUID.randomUUID().toString()

            // Crear una instància de TutorEntity amb un ID únic
            val tutor = TutorEntity(
                id = tutorId,  // Generar un ID únic
                name = firstName,
                surname = lastName,
                email = email,
                password = password
            )

            // Afegir el tutor a la base de dades
            tutorViewModel.insertTutor(tutor)
            navController.navigate("menuTutor") {
                popUpTo("register") { inclusive = true }
                popUpTo("authorize") { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Structure
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Gradient Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.27f) // Ajustem l'alçada del gradient
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(TopGradientStart, TopGradientEnd)
                        )
                    )
                    .padding(start = 24.dp, top = 40.dp, end = 24.dp, bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Benvingut/da\nCrea el teu compte !",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 40.sp,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            // Bottom White Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.84f) // Augmentem l'espai del quadre blanc
                    .background(LightGray)
            )
        }

        // Form Card positioned over the background sections
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 130.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Camp nom
                    TextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("Nom") },
                        placeholder = { Text("Marc", color = HintGrayText) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = DarkGrayText,
                            unfocusedTextColor = DarkGrayText,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = TopGradientEnd,
                            focusedIndicatorColor = TopGradientEnd,
                            unfocusedIndicatorColor = HintGrayText,
                            focusedLabelColor = TopGradientEnd,
                            unfocusedLabelColor = HintGrayText
                        ),
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Nom", tint = HintGrayText)
                        },
                        trailingIcon = {
                            if (firstName.isNotBlank()) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = "Nom", tint = TopGradientEnd)
                            }
                        }
                    )

                    // Camp cognom
                    TextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Cognom") },
                        placeholder = { Text("Gilabert", color = HintGrayText) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = DarkGrayText,
                            unfocusedTextColor = DarkGrayText,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = TopGradientEnd,
                            focusedIndicatorColor = TopGradientEnd,
                            unfocusedIndicatorColor = HintGrayText,
                            focusedLabelColor = TopGradientEnd,
                            unfocusedLabelColor = HintGrayText
                        ),
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Cognom", tint = HintGrayText)
                        },
                        trailingIcon = {
                            if (lastName.isNotBlank()) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = "Cognom", tint = TopGradientEnd)
                            }
                        }
                    )

                    // Camp email
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        placeholder = { Text("exemple@exemple.com", color = HintGrayText) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = DarkGrayText,
                            unfocusedTextColor = DarkGrayText,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = TopGradientEnd,
                            focusedIndicatorColor = TopGradientEnd,
                            unfocusedIndicatorColor = HintGrayText,
                            focusedLabelColor = TopGradientEnd,
                            unfocusedLabelColor = HintGrayText
                        ),
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = HintGrayText)
                        },
                        trailingIcon = {
                            if (email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = "Email", tint = TopGradientEnd)
                            }
                        }
                    )

                    // Camp contrasenya
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contrasenya") },
                        placeholder = { Text("********", color = HintGrayText) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = "Mostrar Contrasenya",
                                    tint = HintGrayText
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = DarkGrayText,
                            unfocusedTextColor = DarkGrayText,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = TopGradientEnd,
                            focusedIndicatorColor = TopGradientEnd,
                            unfocusedIndicatorColor = HintGrayText,
                            focusedLabelColor = TopGradientEnd,
                            unfocusedLabelColor = HintGrayText
                        ),
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "Contrasenya", tint = HintGrayText)
                        },
                    )

                    // Mostrar missatge d'error si la validació falla
                    if (errorMessage.isNotEmpty()) {
                        Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Botó de registre
                    Button(
                        onClick = { handleRegister() },
                        enabled = firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && password.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(ButtonGradientStart, ButtonGradientEnd)
                                    )
                                )
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "REGISTRAR-SE", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botó de tornar enrere sota el Card
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(50)), // Ombra al botó
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White) // Fons blanc
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Tornar enrere",
                    tint = DarkGrayText // Color del botó
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botó de login
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ja tens un compte? ",
                    fontSize = 14.sp,
                    color = DarkGrayText,
                    textDecoration = TextDecoration.Underline
                )
                TextButton(onClick = {
                    navController.navigate("loginForm") {
                        popUpTo("register") { inclusive = true }
                    }
                }) {
                    Text(
                        text = "Inicia Sessió",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TopGradientEnd,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}