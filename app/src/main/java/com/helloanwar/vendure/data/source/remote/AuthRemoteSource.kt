package com.helloanwar.vendure.data.source.remote

import com.helloanwar.vendure.*
import com.helloanwar.vendure.ui.auth.AuthUiState
import javax.inject.Inject

class AuthRemoteSource @Inject constructor() {
    suspend fun login(userName: String, password: String): AuthUiState {
        return try {
            val authenticateMutation =
                NativeAuthenticateMutation(userName = userName, password = password)
            val response = apolloClient.mutation(authenticateMutation).execute()
            response.setLoginToken()
            AuthUiState.Success(response.data!!)
        } catch (e: Exception) {
            AuthUiState.Error(e)
        }
    }

    suspend fun googleLogin(token: String): AuthUiState {
        return try {
            val googleAuthenticate = GoogleAuthenticateMutation(token = token)
            val response = apolloClient.mutation(googleAuthenticate).execute()
            response.setLoginToken()
            AuthUiState.Success(response.data!!)
        } catch (e: Exception) {
            AuthUiState.Error(e)
        }
    }

    suspend fun createCustomer(firstName: String, email: String, password: String): AuthUiState {
        return try {
            val registerNativeMutation =
                RegisterNativeMutation(firstName = firstName, email = email, password = password)
            val response = apolloClient.mutation(registerNativeMutation).execute()
            AuthUiState.Success(response.data!!)
        } catch (e: Exception) {
            AuthUiState.Error(e)
        }
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
        return try {
            val logoutMutation = LogoutMutation()
            val response = apolloClient.mutation(logoutMutation).execute()
            AuthUiState.Success(response.data!!)
        } catch (e: Exception) {
            AuthUiState.Error(e)
        }
    }
}