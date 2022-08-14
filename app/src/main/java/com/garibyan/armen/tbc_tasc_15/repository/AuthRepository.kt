package com.garibyan.armen.tbc_tasc_15.repository

import com.garibyan.armen.tbc_tasc_15.network.ApiService
import com.garibyan.armen.tbc_tasc_15.network.model.LoginRegisterRequest

class AuthRepository(
    private val api: ApiService
) : BaseRepository() {

    suspend fun login(requestBody: LoginRegisterRequest) = apiCall { api.login(requestBody) }

    suspend fun register(requestBody: LoginRegisterRequest) = apiCall { api.register(requestBody) }

}