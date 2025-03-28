package com.example.mysafetracking.data

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.mysafetracking.data.db.entities.TutorEntity
import com.example.mysafetracking.data.db.entities.toDomainModel
import java.time.LocalDateTime
import kotlin.random.Random

// Classe base per a tots els usuaris
open class User(
    open var id: String = "",
    open var name: String = "",
    open var surname: String = "",
    open var email: String = "",
    open var photoProfile: String = ""
)

// Classe per al Tutor
data class Tutor(
    var children: List<Child> = emptyList() // Llista d'IDs dels seus fills
) : User() {
    // Constructor del tutor que crida el constructor de User
    constructor(
        id: String,
        name: String,
        surname: String,
        email: String,
        photoProfile: String,
        children: List<Child>
    ) : this(children) {
        this.id = id
        this.name = name
        this.surname = surname
        this.email = email
        this.photoProfile = photoProfile
    }
}

// Classe per al Tutorat (nen)
data class Child(
    override var id: String = "",
    override var name: String = "",
    override var surname: String = "",
    override var email: String = "",
    override var photoProfile: String = "",
    var guardianId: String = "",
    var currentLocation: Location? = null,
    var childCode: String = generateRandomCode()
) : User(id, name, surname, email, photoProfile) {
    companion object {
        fun generateRandomCode(length: Int = 12): String {
            val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            return (1..length)
                .map { charset.random() }
                .joinToString("")
        }
    }
}

// Classe per emmagatzemar la ubicació amb el timestamp
data class Location(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    val timestamp: String = LocalDateTime.now().toString() // Guarda data i hora actual
)

// Funció per generar una ubicació aleatòria dins d'un rang
fun generateRandomLocation(
    minLat: Double = -90.0, maxLat: Double = 90.0,
    minLon: Double = -180.0, maxLon: Double = 180.0
): Location {
    val latitude = Random.nextDouble(minLat, maxLat)
    val longitude = Random.nextDouble(minLon, maxLon)
    return Location(latitude, longitude)
}

// Imatges
val drawableImages = listOf(
    "gee_me_001",
    "gee_me_002",
    "gee_me_003",
    "gee_me_004",
    "gee_me_005",
    "gee_me_006",
    "gee_me_007",
    "gee_me_008",
    "gee_me_009",
    "gee_me_010",
    "gee_me_011",
    "gee_me_012",
    "gee_me_013",
    "gee_me_014",
    "gee_me_015",
    "gee_me_016",
    "gee_me_017",
    "gee_me_018",
    "gee_me_019",
    "gee_me_020",
    "gee_me_021",
    "gee_me_022",
    "gee_me_023",
    "gee_me_024",
    "gee_me_025",
    "gee_me_026",
    "gee_me_027",
    "gee_me_028",
    "gee_me_029",
    "gee_me_030",
    "gee_me_031",
    "gee_me_032",
)

// Simulació de dades de nens
private var childrenData = mutableListOf(
    Child(
        id = "1",
        name = "Marc",
        surname = "García",
        email = "marc@example.com",
        guardianId = "T1",
        currentLocation = Location(41.3851, 2.1734),
        photoProfile = drawableImages.random(),
    ),
    Child(
        id = "2",
        name = "Clàudia",
        surname = "Martínez",
        email = "claudia@example.com",
        guardianId = "T2",
        currentLocation = Location(41.6176, 0.6200),
        photoProfile = drawableImages.random(),
    ),
    Child(
        id = "3",
        name = "Pau",
        surname = "Fernández",
        email = "pau@example.com",
        guardianId = "T1",
        currentLocation = Location(41.3851, 2.1734),
        photoProfile = drawableImages.random(),
    ),
    Child(
        id = "4",
        name = "Aina",
        surname = "Ruiz",
        email = "aina@example.com",
        guardianId = "T3",
        currentLocation = Location(41.1192, 1.2432),
        photoProfile = drawableImages.random(),
    ),
    Child(
        id = "5",
        name = "Joan",
        surname = "Soler",
        email = "joan@example.com",
        guardianId = "T2",
        currentLocation = Location(41.3851, 2.1734),
        photoProfile = drawableImages.random(),
    ),
    Child(
        id = "6",
        name = "Marta",
        surname = "Lloret",
        email = "marta@example.com",
        guardianId = "T1",
        currentLocation = Location(43.357778, 2.458611),
        photoProfile = drawableImages.random(),
    ),
    Child(
        id = "7",
        name = "Miquel",
        surname = "Adria",
        email = "miquel@example.com",
        guardianId = "T1",
        currentLocation = Location(44.3543132, 0.458611),
        photoProfile = drawableImages.random(),
    ),
    Child(
        id = "8",
        name = "Raul",
        surname = "Aitor",
        email = "raul@example.com",
        guardianId = "T1",
        currentLocation = Location(42.542311, 1.54211),
        photoProfile = drawableImages.random(),
    ),
    Child(
        id = "9",
        name = "Maria",
        surname = "Aitona",
        email = "maria@example.com",
        guardianId = "T1",
        currentLocation = Location(42.357778, 1.458611),
        photoProfile = drawableImages.random(),
    )
)

fun getChildren(): List<Child> {
    return childrenData
}

// Eliminar un fill per ID
fun removeChild(childId: String) {
    childrenData.removeIf { it.id == childId }
}


val tutorData by lazy { Tutor() }

fun setTutor(tutorEntity: TutorEntity)  {
    tutorData.id = tutorEntity.id
    tutorData.name = tutorEntity.name
    tutorData.surname = tutorEntity.surname
    tutorData.email = tutorEntity.email
    tutorData.photoProfile = tutorEntity.photoProfile
    tutorData.children = tutorEntity.children.map { it.toDomainModel() }
}