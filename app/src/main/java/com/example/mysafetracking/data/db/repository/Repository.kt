package com.example.mysafetracking.data.db.repository

import com.example.mysafetracking.data.db.dao.ChildDao
import com.example.mysafetracking.data.db.dao.LocationDao
import com.example.mysafetracking.data.db.dao.TutorDao
import com.example.mysafetracking.data.db.entities.ChildEntity
import com.example.mysafetracking.data.db.entities.ChildWithLocations
import com.example.mysafetracking.data.db.entities.LocationEntity
import com.example.mysafetracking.data.db.entities.TutorEntity

// Repositori
class TutorRepository(private val tutorDao: TutorDao, private val childDao: ChildDao) {
    suspend fun getTutor(email: String): TutorEntity? = tutorDao.getTutorByEmail(email)
    suspend fun insertTutor(tutor: TutorEntity) = tutorDao.insertTutor(tutor)
    suspend fun getChildById(childId: String): ChildEntity? {
        return childDao.getChildById(childId)
    }
}

class ChildRepository(private val childDao: ChildDao) {
    suspend fun getChildWithLocations(childId: String): ChildWithLocations? {
        return childDao.getChildWithLocations(childId)
    }

    suspend fun insertChild(child: ChildEntity) = childDao.insertChild(child)

    // Funció per obtenir els fills d'un tutor per guardianId
    suspend fun getChildrenForGuardian(guardianId: String): List<ChildWithLocations> {
        return childDao.getChildrenForGuardian(guardianId)  // Passar el guardianId per filtrar els fills
    }
}


class LocationRepository(private val locationDao: LocationDao) {
    suspend fun insertLocation(location: LocationEntity) = locationDao.insertLocation(location)
    suspend fun getChildLocations(childId: String): List<LocationEntity> = locationDao.getLocationsForChild(childId)
}
