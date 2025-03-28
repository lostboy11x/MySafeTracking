package com.example.mysafetracking.data


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
fun generateRandomLocation(): Location {
    // Rangs de coordenades per Catalunya
    val minLat = 41.6176
    val maxLat = 42.7043
    val minLon = 0.6226
    val maxLon = 2.8216

    // Generem coordenades aleatòries dins del rang definit
    val latitude = Random.nextDouble(minLat, maxLat)
    val longitude = Random.nextDouble(minLon, maxLon)

    return Location(latitude, longitude)
}

// Imatges
object DrawableImages {
    val list = listOf(
        "gee_me_001", "gee_me_002", "gee_me_003", "gee_me_004", "gee_me_005",
        "gee_me_006", "gee_me_007", "gee_me_008", "gee_me_009", "gee_me_010",
        "gee_me_011", "gee_me_012", "gee_me_013", "gee_me_014", "gee_me_015",
        "gee_me_016", "gee_me_017", "gee_me_018", "gee_me_019", "gee_me_020",
        "gee_me_021", "gee_me_022", "gee_me_023", "gee_me_024", "gee_me_025",
        "gee_me_026", "gee_me_027", "gee_me_028", "gee_me_029", "gee_me_030",
        "gee_me_031", "gee_me_032"
    )
}

fun getRandonImage(): String {
    return  DrawableImages.list.random()
}

// Funció per generar un nou fill
fun createChild(
    id: String, name: String, surname: String, email: String, guardianId: String,
    latitude: Double, longitude: Double
): Child {
    return Child(
        id = id,
        name = name,
        surname = surname,
        email = email,
        guardianId = guardianId,
        currentLocation = Location(latitude, longitude),
        photoProfile = getRandonImage()
    )
}

// Llista de fills
private val childrenData = mutableListOf(
    createChild("1", "Marc", "García", "marc@example.com", "T1", 41.3851, 2.1734),
    createChild("2", "Clàudia", "Martínez", "claudia@example.com", "T2", 41.6176, 0.6200),
    createChild("3", "Pau", "Fernández", "pau@example.com", "T1", 41.3851, 2.1734),
    createChild("4", "Aina", "Ruiz", "aina@example.com", "T3", 41.1192, 1.2432),
    createChild("5", "Joan", "Soler", "joan@example.com", "T2", 41.3851, 2.1734),
    createChild("6", "Marta", "Lloret", "marta@example.com", "T1", 43.357778, 2.458611),
    createChild("7", "Miquel", "Adria", "miquel@example.com", "T1", 44.3543132, 0.458611),
    createChild("8", "Raul", "Aitor", "raul@example.com", "T1", 42.542311, 1.54211),
    createChild("9", "Maria", "Aitona", "maria@example.com", "T1", 42.357778, 1.458611)
)

fun getChildren(): List<Child> {
    return childrenData
}

// Eliminar un fill per ID
fun removeChild(childId: String) {
    childrenData.removeIf { it.id == childId }
}


val tutorData by lazy { Tutor() }

fun setTutor(tutorEntity: TutorEntity) {
    tutorData.id = tutorEntity.id
    tutorData.name = tutorEntity.name
    tutorData.surname = tutorEntity.surname
    tutorData.email = tutorEntity.email
    tutorData.photoProfile = tutorEntity.photoProfile
    tutorData.children = tutorEntity.children.map { it.toDomainModel() }
}