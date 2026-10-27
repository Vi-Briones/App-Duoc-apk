package com.example.mastermartini.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mastermartini.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginSuccess = MutableStateFlow<Boolean?>(null)
    val loginSuccess: StateFlow<Boolean?> = _loginSuccess

    private val _registerSuccess = MutableStateFlow<Boolean?>(null)
    val registerSuccess: StateFlow<Boolean?> = _registerSuccess

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginSuccess.value = repository.login(email, pass)
            _isLoading.value = false
        }
    }

    fun register(name: String, email: String, rut: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _registerSuccess.value = repository.register(name, email, rut, pass)
            _isLoading.value = false
        }
    }

    fun resetRegisterState() {
        _registerSuccess.value = null
    }
}