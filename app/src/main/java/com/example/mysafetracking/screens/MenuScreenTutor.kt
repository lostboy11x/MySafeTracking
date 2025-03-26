package com.example.mysafetracking.screens

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mysafetracking.R
import com.example.mysafetracking.data.Child
import com.example.mysafetracking.data.getChildren

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreenTutor(navController: NavHostController) {
    var children by remember { mutableStateOf(getChildren().toMutableList()) }
    var isEditing by remember { mutableStateOf(false) }

    fun onSaveChild(updatedChild: Child) {
        // Actualitzem la llista de nens amb el nen editat
        children = children.map {
            if (it.id == updatedChild.id) updatedChild else it
        }.toMutableList()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name), color = Color.White)
                },
                actions = {
                    TextButton(onClick = { isEditing = !isEditing }) {
                        Text(if (isEditing) "Fet" else "Edita", color = Color.White)
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botó per veure el mapa
            Button(
                onClick = { navController.navigate("mapScreen") { popUpTo("menuTutor") } },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Veure el mapa", fontWeight = FontWeight.Bold)
            }

            // Llista de nens amb mode d'edició
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(children, key = { it.id }) { child ->
                    ChildItemEditable(
                        child = child,
                        isEditing = isEditing,
                        onRemove = { childToRemove ->
                            children = children.filter { it.id != childToRemove.id }.toMutableList()
                        },
                        onSave = { updatedChild -> onSaveChild(updatedChild) },
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun ChildItemEditable(
    child: Child,
    isEditing: Boolean,
    onRemove: (Child) -> Unit,
    onSave: (Child) -> Unit,
    navController: NavHostController
) {
    val imageResource = getImageResource(child.photoProfile)
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                if (!isEditing) {
                    navController.navigate("childInformationScreen/${child.currentLocation?.latitude}/${child.currentLocation?.longitude}")
                } else {
                    //Pop UP editar
                    showEditDialog = true
                }

            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)) // Blau clar suau
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(Color(0xFFE3F2FD))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEditing) {
                IconButton(onClick = { onRemove(child) }) {
                    Icon(
                        imageVector = Icons.Filled.RemoveCircle,
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }
            }

            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "Foto de perfil del nen",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${child.name} ${child.surname}",
                    fontWeight = FontWeight.Bold
                )
                child.currentLocation?.let {
                    Text(
                        text = "Ubicació: Lat ${it.latitude}, Lng ${it.longitude}",
                        color = Color.Gray
                    )
                } ?: Text(text = "Ubicació no disponible", color = Color.Gray)
            }
        }
    }

    if (showEditDialog) {
        EditChildDialog(
            child = child,
            onDismiss = { showEditDialog = false },
            onSave = {
                onSave(it)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EditChildDialog(child: Child, onDismiss: () -> Unit, onSave: (Child) -> Unit) {
    var name by remember { mutableStateOf(child.name) }
    var surname by remember { mutableStateOf(child.surname) }
    var childCode by remember { mutableStateOf(child.childCode) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Nen") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Cognoms") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = childCode,
                    onValueChange = { childCode = it },
                    label = { Text("Codi de nen") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(child.copy(
                    name = name,
                    surname = surname,
                    email = child.email, // Mantenim el mateix email
                    photoProfile = child.photoProfile, // Mantenim la mateixa foto
                    guardianId = child.guardianId,
                    currentLocation = child.currentLocation, // Deixem la mateixa ubicació
                    childCode = childCode
                ))
            }) {
                Text("Desar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel·lar")
            }
        }
    )
}

// Funció per obtenir la imatge del recurs drawable en base al nom
@Composable
fun getImageResource(imageName: String): Int {
    val context = LocalContext.current
    // Obtenim el recurs de drawable a partir del nom de la imatge
    val resId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
    return if (resId != 0) resId else R.drawable.gee_me_002 // Retornem la imatge per defecte si no existeix
}

