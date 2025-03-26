package com.example.mysafetracking.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Entitats de la base de dades
@Entity
data class TutorEntity(
    @PrimaryKey val id: String, // immutable
    var name: String = "",      // mutable
    var surname: String = "",   // mutable
    var email: String = "",     // mutable
    var photoProfile: String = "", // mutable
    var children: List<String> = emptyList() // mutable
)

@Entity
data class ChildEntity(
    @PrimaryKey val id: String, // immutable
    var name: String = "",      // mutable
    var surname: String = "",   // mutable
    var email: String = "",     // mutable
    var photoProfile: String = "", // mutable
    var guardianId: String = "", // mutable
    var childCode: String = "" // mutable
)

data class ChildWithLocations(
    @Embedded val child: ChildEntity,  // El nen
    @Relation(
        parentColumn = "id",  // La columna de l'entitat pare (ChildEntity)
        entityColumn = "childId"  // La columna de l'entitat relacionada (LocationEntity)
    )
    val locations: List<LocationEntity>  // Totes les ubicacions relacionades amb aquest nen
)


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ChildEntity::class,
            parentColumns = ["id"],
            childColumns = ["childId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val locationId: Int = 0,
    val childId: String,  // ID del nen (per relacionar amb ChildEntity)
    var latitude: Double,  // mutable
    var longitude: Double, // mutable
    var timestamp: String = LocalDateTime.now().toString() // mutable
)

class Converters {
    @TypeConverter
    fun fromLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
    }

    @TypeConverter
    fun toLocalDateTime(date: LocalDateTime?): String? {
        return date?.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    @TypeConverter
    fun fromListToString(value: List<String>?): String? {
        val gson = Gson()
        return gson.toJson(value) // no necesitas TypeToken para List<String>
    }

    @TypeConverter
    fun fromStringToList(value: String?): List<String> {
        val gson = Gson()
        return gson.fromJson(value, Array<String>::class.java).toList() // Convierte el JSON a un Array y luego a List
    }

}