package com.example.mysafetracking.screens.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mysafetracking.R

@Composable
fun AuthorizeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Text "Benvingut a MySafeTracking"
            Text(
                text = "Benvingut a ${stringResource(R.string.app_name)}",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Botó "Iniciar Sessió"
            Button(
                onClick = { navController.navigate("loginForm") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp, max = 56.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Iniciar Sessió", fontSize = 18.sp)
            }

            // Botó "Crear un Compte"
            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp, max = 56.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Crear un Compte", fontSize = 18.sp)
            }

            // Botó Iniciar com a fill
            TextButton(
                onClick = {
                    //navController.popBackStack()
                    navController.navigate("logincChild")
                },
                modifier = Modifier
                    .heightIn(min = 48.dp, max = 56.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Sóc un fill", fontSize = 18.sp)
            }
        }
    }
}