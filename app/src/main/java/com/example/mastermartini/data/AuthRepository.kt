package com.example.mastermartini.data

import kotlinx.coroutines.delay

class AuthRepository {

    companion object {
        private var currentEmail = "usuario@mastermartini.cl"
        private var currentPass = "123456"
    }

    suspend fun login(email: String, pass: String): Boolean {
        delay(1500) // Simula tiempo de red
        return email == currentEmail && pass == currentPass
    }

    suspend fun register(name: String, email: String, rut: String, pass: String): Boolean {
        delay(1500)

        if (name.isNotBlank() && email.isNotBlank() && rut.isNotBlank() && pass.isNotBlank()) {
            // Validamos que el correo termine obligatoriamente en @mastermartini.cl
            if (email.trim().endsWith("@mastermartini.cl")) {
                currentEmail = email.trim()
                currentPass = pass
                return true
            }
        }
        return false
    }
}