package com.helloanwar.vendure.ui.auth

import androidx.lifecycle.*
import com.apollographql.apollo3.api.Mutation
import com.helloanwar.vendure.data.source.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authUiState = MutableLiveData<AuthUiState>()
    val authUiState: LiveData<AuthUiState>
        get() = _authUiState

    private val _createCustomerState = MutableLiveData<AuthUiState>()
    val createCustomerState: LiveData<AuthUiState>
        get() = _createCustomerState

    fun googleLogin(token: String) = viewModelScope.launch {
        _authUiState.value = AuthUiState.Loading
        _authUiState.value = authRepository.googleLogin(token)
    }

    fun login(userName: String, password: String) = viewModelScope.launch {
        _authUiState.value = AuthUiState.Loading
        _authUiState.value = authRepository.login(userName, password)
    }

    fun createCustomer(firstName: String, email: String, password: String) = viewModelScope.launch {
        _createCustomerState.value = AuthUiState.Loading
        _createCustomerState.value = authRepository.createCustomer(
            firstName = firstName, email = email, password = password
        )
    }
}

sealed class AuthUiState {
    data class Success(val data: Mutation.Data) : AuthUiState()
    data class Error(val exception: Throwable) : AuthUiState()
    object Loading : AuthUiState()
}

