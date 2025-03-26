package com.example.mysafetracking.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mysafetracking.data.db.entities.ChildEntity
import com.example.mysafetracking.data.db.entities.ChildWithLocations
import com.example.mysafetracking.data.db.entities.LocationEntity
import com.example.mysafetracking.data.db.entities.TutorEntity

// DAO
@Dao
interface TutorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTutor(tutor: TutorEntity)

    @Query("SELECT * FROM TutorEntity WHERE id = :tutorId")
    suspend fun getTutorById(tutorId: String): TutorEntity?
}

@Dao
interface ChildDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChild(child: ChildEntity)

    @Query("SELECT * FROM ChildEntity WHERE id = :childId")
    suspend fun getChildById(childId: String): ChildEntity?

    @Transaction
    @Query("SELECT * FROM ChildEntity WHERE id = :childId")
    suspend fun getChildWithLocations(childId: String): ChildWithLocations
}

@Dao
interface LocationDao {
    @Insert
    suspend fun insertLocation(location: LocationEntity)

    @Update
    suspend fun updateLocation(location: LocationEntity)

    @Delete
    suspend fun deleteLocation(location: LocationEntity)

    @Query("SELECT * FROM LocationEntity WHERE childId = :childId")
    suspend fun getLocationsForChild(childId: String): List<LocationEntity>

    @Query("SELECT * FROM LocationEntity WHERE locationId = :locationId")
    suspend fun getLocationById(locationId: Int): LocationEntity?
}
