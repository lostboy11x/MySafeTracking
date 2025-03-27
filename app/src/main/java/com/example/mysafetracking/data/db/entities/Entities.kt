package com.example.mysafetracking.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.mysafetracking.data.Child
import com.example.mysafetracking.data.Location
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
    val password: String = "",
    var photoProfile: String = "", // mutable
    @TypeConverters(Converters::class)
    var children: List<ChildEntity> = emptyList() // mutable
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
fun Child.toEntity(): ChildEntity {
    return ChildEntity(
        id = this.id,
        name = this.name,
        surname = this.surname,
        email = this.email,
        photoProfile = this.photoProfile,
        guardianId = this.guardianId,
        childCode = this.childCode
    )
}

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
    private val gson = Gson()
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

    @TypeConverter
    fun fromListToJson(value: List<ChildEntity>?): String? {
        // Converteix la llista de ChildEntity a JSON
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromJsonToList(value: String?): List<ChildEntity>? {
        // Converteix el JSON a una llista de ChildEntity
        val type = object : TypeToken<List<ChildEntity>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromLocation(location: Location?): String? {
        return location?.let { "${it.latitude},${it.longitude}" }
    }

    @TypeConverter
    fun toLocation(data: String?): Location? {
        return data?.split(",")?.let {
            Location(
                latitude = it[0].toDouble(),
                longitude = it[1].toDouble(),
                timestamp = it[2]
            )
        }
    }

}