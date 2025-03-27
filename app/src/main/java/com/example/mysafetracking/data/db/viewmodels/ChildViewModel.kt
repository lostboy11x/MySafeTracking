package com.example.mysafetracking.data.db.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysafetracking.data.Child
import com.example.mysafetracking.data.db.dao.ChildDao
import com.example.mysafetracking.data.db.database.AppDatabase
import com.example.mysafetracking.data.db.entities.ChildEntity
import com.example.mysafetracking.data.db.entities.ChildWithLocations
import com.example.mysafetracking.data.db.entities.toEntity
import com.example.mysafetracking.data.db.repository.ChildRepository
import kotlinx.coroutines.launch

class ChildViewModel(private val childRepository: ChildRepository, private val database: AppDatabase) : ViewModel() {
    // LiveData per la llista de fills
    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> get() = _children
    private var tutorId: String = ""
    private val childDao: ChildDao = database.childDao()

    init {
        loadChildren(tutorId)  // Carregar la llista de fills quan es crea el ViewModel
    }

    // Funció per carregar la llista de fills des de la base de dades
    fun loadChildren(tutorId: String) {
        viewModelScope.launch {
            val childrenWithLocations = childRepository.getChildrenForGuardian(tutorId)
            _children.value = childrenWithLocations.map { it.toDomainModel() }
        }
    }

    // Funció per actualitzar un fill a la base de dades
    fun updateChild(child: Child) {
        viewModelScope.launch {
            val childEntity = child.toEntity()
            childDao.updateChild(childEntity)  // Suponem que tens una funció d'actualització al DAO
            // Opcionalment, actualitzar la llista de fills a la UI
            _children.value = childDao.getChildrenByTutor(child.guardianId)  // Si cal, actualitzar la llista després de l'actualització
        }
    }

    // Funció per eliminar un fill de la base de dades
    fun removeChild(child: Child) {
        viewModelScope.launch {
            val childEntity = child.toEntity()
            childDao.deleteChild(childEntity)  // Funció de deleteChild al DAO
            // Opcionalment, actualitzar la llista de fills a la UI
            _children.value = childDao.getChildrenByTutor(child.guardianId)  // Si cal, actualitzar la llista després de l'eliminació
        }
    }

    // Funció per obtenir un fill per ID
    fun getChild(childId: String, onResult: (Child?) -> Unit) {
        viewModelScope.launch {
            val childWithLocations = childRepository.getChildWithLocations(childId)
            onResult(childWithLocations?.toDomainModel())
        }
    }

    // Funció per inserir un fill a la base de dades
    fun insertChild(child: ChildEntity) {
        viewModelScope.launch {
            childRepository.insertChild(child)
            loadChildren(child.guardianId)  // Torna a carregar els fills del tutor després de la inserció
        }
    }
    fun setTutorIde(tutorId: String) {
        this.tutorId = tutorId
    }
    fun getTutorId(): String {
        return tutorId
    }
}
fun ChildWithLocations.toDomainModel(): Child {
    return Child(
        id = this.child.id,
        name = this.child.name,
        surname = this.child.surname,
        email = this.child.email,
        photoProfile = this.child.photoProfile,
        guardianId = this.child.guardianId,
        currentLocation = if (this.locations.isNotEmpty()) {
            // Si hi ha ubicacions associades, utilitzem la primera ubicació (pots modificar aquesta lògica)
            this.locations.first().toDomainModel()
        } else {
            null
        },
        childCode = this.child.childCode
    )
}

