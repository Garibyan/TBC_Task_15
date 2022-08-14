package com.garibyan.armen.tbc_tasc_15.screens.registerscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garibyan.armen.tbc_tasc_15.network.ApiClient
import com.garibyan.armen.tbc_tasc_15.network.Resource
import com.garibyan.armen.tbc_tasc_15.network.model.LoginRegisterRequest
import com.garibyan.armen.tbc_tasc_15.network.model.RegisterResponse
import com.garibyan.armen.tbc_tasc_15.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository = AuthRepository(ApiClient.apiService)
) : ViewModel() {

    private val _registerState = MutableStateFlow<Resource<RegisterResponse>>(Resource.Loading)
    val registerState = _registerState.asStateFlow()

    fun register(email: String, password: String) {
        val registerRequest = LoginRegisterRequest(email = email, password = password)
        viewModelScope.launch {
            repository.register(registerRequest).collectLatest {
                _registerState.value = it
            }
        }
    }


}