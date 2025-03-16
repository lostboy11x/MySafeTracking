package com.example.mysafetracking.logic

import android.util.Patterns

// Funció per validar el login
fun validateLogin(email: String, password: String): Boolean {
    return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotBlank()
}

// Funció per validar el registre
fun validateRegister(firstName: String, lastName: String, email: String, password: String): Boolean {
    return firstName.isNotBlank() && lastName.isNotBlank() &&
            email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            password.isNotBlank()
}