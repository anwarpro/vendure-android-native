package com.helloanwar.vendure.data.source

import com.helloanwar.vendure.data.source.remote.AuthRemoteSource
import com.helloanwar.vendure.ui.auth.AuthUiState
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remoteSource: AuthRemoteSource
) {
    suspend fun login(userName: String, password: String): AuthUiState {
        return remoteSource.login(userName, password)
    }

    suspend fun googleLogin(token: String): AuthUiState {
        return remoteSource.googleLogin(token)
    }

    suspend fun createCustomer(firstName: String, email: String, password: String): AuthUiState {
        return remoteSource.createCustomer(
            firstName = firstName,
            email = email,
            password = password
        )
    }

    suspend fun verifyCustomerAccount() {

    }

    suspend fun getCurrentUser() {

    }

    suspend fun forgetPassword() {

    }

    suspend fun verifyForgetPassword() {

    }

    suspend fun sendOTP() {

    }

    suspend fun verifyOTP() {

    }

    suspend fun logout(): AuthUiState {
        return remoteSource.logout()
    }
}