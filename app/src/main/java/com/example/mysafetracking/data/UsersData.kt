package com.example.mysafetracking.data

import java.time.LocalDateTime

// Classe base per a tots els usuaris
open class User(
    val id: String = "",
    val name: String = "",
    val lastname: String = "",
    val email: String = ""
)

// Classe per al Tutor
data class Tutor(
    val children: List<String> = emptyList() // Llista d'IDs dels seus fills
) : User()

// Classe per al Tutorat (nen)
data class Child(
    val guardianId: String = "",
    val currentLocation: Location? = null // Ubicació actual
) : User()

// Classe per emmagatzemar la ubicació amb el timestamp
data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timestamp: LocalDateTime = LocalDateTime.now() // Guarda data i hora actual
)