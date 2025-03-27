package com.example.mysafetracking.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.mysafetracking.data.db.entities.toEntity
import com.example.mysafetracking.data.db.viewmodels.ChildViewModel
import com.example.mysafetracking.data.db.viewmodels.TutorViewModel
import com.example.mysafetracking.data.getChildren

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreenTutor(navController: NavHostController, tutorViewModel: TutorViewModel, childViewModel: ChildViewModel) {
    val tutor by tutorViewModel.tutor.observeAsState()
    //var children by remember { mutableStateOf(getChildren().toMutableList()) }
    val childrenState by childViewModel.children.observeAsState(initial = emptyList())
    var children by remember { mutableStateOf(childrenState) }
    var isEditing by remember { mutableStateOf(false) }

    if (childViewModel.getTutorId() != "") {
        tutor?.let { childViewModel.setTutorIde(it.id) }
    }

    // Obtenir els fills associats al tutor quan el tutor es carrega
    LaunchedEffect(tutor) {
        tutor?.let {
            childViewModel.loadChildren(it.id) // Carreguem els nens del tutor
        }
    }

    fun onSaveChild(updatedChild: Child) {
        // Actualitzem la llista de nens amb el nen editat
        children = children.map {
            if (it.id == updatedChild.id) updatedChild else it
        }.toMutableList()

        childViewModel.updateChild(updatedChild)
        Log.d("MenuScreenTutor", "Child updated: $updatedChild")
    }

    // Variables per al Popup
    var isDialogOpen by remember { mutableStateOf(false) }
    var childName by remember { mutableStateOf("") }
    var childSurname by remember { mutableStateOf("") }
    var childEmail by remember { mutableStateOf("") }
    var childPhotoProfile by remember { mutableStateOf("") }

    // Funció per afegir el fill i guardar-lo a la base de dades
    fun addChildToDatabase() {
        val newChild = tutor?.let {
            Child(
                name = childName,
                surname = childSurname,
                email = childEmail,
                photoProfile = childPhotoProfile,
                guardianId = it.id,
                childCode = Child.generateRandomCode()
            )
        }

        if (newChild != null) {
            childViewModel.insertChild(newChild.toEntity())
        }
        isDialogOpen = false
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name), color = Color.White)
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 54.dp)
                    .padding(top = 22.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("mapScreen") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Place, contentDescription = "Veure el mapa")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mapa")
                }

                ExtendedFloatingActionButton(
                    onClick = { isEditing = !isEditing },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = "Editar"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isEditing) "Fet" else "Editar")
                }

                // Afegim el fills
                ExtendedFloatingActionButton(
                    onClick = { isDialogOpen = true }, // Mostrem el popup per afegir un fill
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Afegir Fill/a")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Afegir")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Llista de nens amb mode d'edició
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(children, key = { it.id }) { child ->
                    ChildItemEditable(
                        child = child,
                        isEditing = isEditing,
                        onRemove = { childToRemove ->
                            // Actualitzem la llista de nens després de la remoció
                            children =
                                children.filter { it.id != childToRemove.id }.toMutableList()
                            childViewModel.removeChild(childToRemove)
                        },
                        onSave = { updatedChild -> onSaveChild(updatedChild) },
                        navController = navController
                    )
                }
            }
        }
    }

    // Popup per afegir un fill
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            title = { Text("Afegir Fill") },
            text = {
                Column {
                    TextField(
                        value = childName,
                        onValueChange = { childName = it },
                        label = { Text("Nom") }
                    )
                    TextField(
                        value = childSurname,
                        onValueChange = { childSurname = it },
                        label = { Text("Cognom") }
                    )
                    TextField(
                        value = childEmail,
                        onValueChange = { childEmail = it },
                        label = { Text("Correu electrònic") }
                    )
                    TextField(
                        value = childPhotoProfile,
                        onValueChange = { childPhotoProfile = it },
                        label = { Text("Foto de perfil") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        addChildToDatabase() // Afegim el fill i el guardem a la base de dades
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { isDialogOpen = false }
                ) {
                    Text("Cancel·lar")
                }
            }
        )
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
                onSave(
                    child.copy(
                        name = name,
                        surname = surname,
                        email = child.email, // Mantenim el mateix email
                        photoProfile = child.photoProfile, // Mantenim la mateixa foto
                        guardianId = child.guardianId,
                        currentLocation = child.currentLocation, // Deixem la mateixa ubicació
                        childCode = childCode
                    )
                )
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

