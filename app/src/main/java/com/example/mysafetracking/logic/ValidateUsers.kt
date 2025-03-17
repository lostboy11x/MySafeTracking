package com.example.mysafetracking.logic

import android.util.Patterns

// Funció per validar el login
fun validateLogin(email: String, password: String): String {
    return when {
        email.isBlank() -> "L'email no pot estar buit"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Email no vàlid"
        password.isBlank() -> "La contrasenya no pot estar buida"
        password.length < 8 -> "La contrasenya ha de tenir almenys 8 caràcters"
        else -> ""
    }
}

// Funció per validar el registre
fun validateRegister(firstName: String, lastName: String, email: String, password: String): String {
    return when {
        firstName.isBlank() -> "El nom no pot estar buit"
        lastName.isBlank() -> "El cognom no pot estar buit"
        email.isBlank() -> "L'email no pot estar buit"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Email no vàlid"
        password.isBlank() -> "La contrasenya no pot estar buida"
        password.length < 8 -> "La contrasenya ha de tenir almenys 8 caràcters"
        else -> ""
    }
}
