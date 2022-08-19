package com.garibyan.armen.tbc_tasc_15.screens.loginscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garibyan.armen.tbc_tasc_15.network.ApiClient
import com.garibyan.armen.tbc_tasc_15.network.Resource
import com.garibyan.armen.tbc_tasc_15.network.model.LoginRegisterRequest
import com.garibyan.armen.tbc_tasc_15.network.model.LoginResponse
import com.garibyan.armen.tbc_tasc_15.repository.AuthRepository
import com.garibyan.armen.tbc_tasc_15.repository.DataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
    = AuthRepository(ApiClient.apiService)
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<LoginResponse>>(Resource.Loading)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        val loginRequestBody = LoginRegisterRequest(email = email, password = password)
        viewModelScope.launch {
            repository.login(loginRequestBody).collect {
                _loginState.value = it
            }
        }
    }
}