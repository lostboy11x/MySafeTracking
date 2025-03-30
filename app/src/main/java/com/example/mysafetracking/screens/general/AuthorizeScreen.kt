package com.example.mysafetracking.screens.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mysafetracking.R
import com.example.mysafetracking.ui.theme.DarkGrayText
import com.example.mysafetracking.ui.theme.TopGradientEnd
import com.example.mysafetracking.ui.theme.TopGradientStart

@Composable
fun AuthorizeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(TopGradientStart, TopGradientEnd)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Imatge del logo (sense background blanc)
            Image(
                painter = painterResource(id = R.drawable.final_logo_bck),
                contentDescription = "Logo",
                modifier = Modifier.size(400.dp)
            )

            // Card que conté el text i els botons
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Text "Benvingut/da"
                    Text(
                        text = "Benvingut/da",
                        fontSize = 28.sp,
                        color = DarkGrayText,
                        fontWeight = FontWeight.Bold
                    )

                    // Botó "INICIAR SESSIÓ"
                    Button(
                        onClick = { navController.navigate("loginForm") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 16.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(50)),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = TopGradientEnd)
                    ) {
                        Text(text = "INICIAR SESSIÓ", fontSize = 18.sp, color = Color.White)
                    }

                    // Botó "REGISTRAR-SE"
                    Button(
                        onClick = { navController.navigate("register") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 16.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(50)),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = TopGradientEnd)
                    ) {
                        Text(text = "REGISTRAR-SE", fontSize = 18.sp, color = Color.White)
                    }

                    // Botó "SÓC UN FILL/A"
                    Button(
                        onClick = { navController.navigate("logincChild") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 16.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(50)),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = TopGradientEnd)
                    ) {
                        Text(text = "SÓC UN FILL/A", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }
    }
}