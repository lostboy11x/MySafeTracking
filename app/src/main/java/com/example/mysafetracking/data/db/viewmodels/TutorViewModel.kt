package com.example.mysafetracking.data.db.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysafetracking.data.Child
import com.example.mysafetracking.data.Tutor
import com.example.mysafetracking.data.db.entities.ChildEntity
import com.example.mysafetracking.data.db.entities.TutorEntity
import com.example.mysafetracking.data.db.repository.TutorRepository
import kotlinx.coroutines.launch

class TutorViewModel(private val tutorRepository: TutorRepository) : ViewModel() {
    private val _tutor = MutableLiveData<Tutor>()
    val tutor: LiveData<Tutor> get() = _tutor

    fun loadTutor(tutorEntity: TutorEntity) {
        viewModelScope.launch {
            val childrenList = tutorEntity.children.mapNotNull { childId ->
                // Cridar el repositori per obtenir les dades completes dels nens
                tutorRepository.getChildById(childId.toString())  // Obtenir el nen complet
            }
            _tutor.value = tutorEntity.toDomainModel(childrenList)
        }
    }

    fun getTutor(email: String, onResult: (TutorEntity?) -> Unit) {
        viewModelScope.launch {
            val tutor = tutorRepository.getTutor(email)
            onResult(tutor)
        }
    }

    fun insertTutor(tutor: TutorEntity) {
        viewModelScope.launch {
            tutorRepository.insertTutor(tutor)
        }
    }
}

fun TutorEntity.toDomainModel(childrenList: List<ChildEntity>): Tutor {
    // Converteix TutorEntity a Tutor, afegint les dades completes dels nens
    val children = childrenList.map { childEntity ->
        Child(
            id = childEntity.id,
            name = childEntity.name,
            surname = childEntity.surname,
            email = childEntity.email,
            photoProfile = childEntity.photoProfile
        )
    }

    return Tutor(
        id = this.id,
        name = this.name,
        surname = this.surname,
        email = this.email,
        photoProfile = this.photoProfile,
        children = children
    )
}

