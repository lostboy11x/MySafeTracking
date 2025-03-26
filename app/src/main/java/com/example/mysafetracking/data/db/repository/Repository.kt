package com.example.mysafetracking.data.db.repository

import com.example.mysafetracking.data.db.dao.ChildDao
import com.example.mysafetracking.data.db.dao.LocationDao
import com.example.mysafetracking.data.db.dao.TutorDao
import com.example.mysafetracking.data.db.entities.ChildEntity
import com.example.mysafetracking.data.db.entities.LocationEntity
import com.example.mysafetracking.data.db.entities.TutorEntity

// Repositori

class TutorRepository(private val tutorDao: TutorDao) {
    suspend fun getTutor(tutorId: String): TutorEntity? = tutorDao.getTutorById(tutorId)
    suspend fun insertTutor(tutor: TutorEntity) = tutorDao.insertTutor(tutor)
}

class ChildRepository(private val childDao: ChildDao) {
    suspend fun getChild(childId: String): ChildEntity? = childDao.getChildById(childId)
    suspend fun insertChild(child: ChildEntity) = childDao.insertChild(child)
}

class LocationRepository(private val locationDao: LocationDao) {
    suspend fun insertLocation(location: LocationEntity) = locationDao.insertLocation(location)
    suspend fun getChildLocations(childId: String): List<LocationEntity> = locationDao.getLocationsForChild(childId)
}
