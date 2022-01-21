package com.helloanwar.vendure

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.helloanwar.vendure.data.source.AuthRepository
import com.helloanwar.vendure.data.source.CartRepository
import com.helloanwar.vendure.ui.auth.AuthUiState
import com.helloanwar.vendure.ui.home.HomeScreenUiState
import com.helloanwar.vendure.util.SharedPrefUtil
import com.helloanwar.vendure.util.getToken
import com.helloanwar.vendure.util.setToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val sharedPrefUtil: SharedPrefUtil,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _addToCart = MutableLiveData<CartUiState>()
    val addToCart: LiveData<CartUiState>
        get() = _addToCart

    private val _activeOrder = MutableLiveData<HomeScreenUiState>()
    val activeOrder: LiveData<HomeScreenUiState>
        get() = _activeOrder

    private val _authUiState = MutableLiveData<AuthUiState>()
    val authUiState: LiveData<AuthUiState>
        get() = _authUiState

    fun addToCart(variantId: String, qty: Int) = viewModelScope.launch {
        _addToCart.value = CartUiState.Loading
        _addToCart.value = cartRepository.addItemToCart(variantId, qty)
        getActiveOrder()
    }

    private fun getActiveOrder() = viewModelScope.launch {
        _activeOrder.value = HomeScreenUiState.Loading
        _activeOrder.value = cartRepository.getActiveOrder()
    }

    private val firebaseAuth = Firebase.auth

    @ExperimentalCoroutinesApi
    fun firebaseUserFlow() = callbackFlow {
        val authStatListener = FirebaseAuth.AuthStateListener {
            trySend(it.currentUser)
        }
        firebaseAuth.addAuthStateListener(authStatListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStatListener)
        }
    }

    @ExperimentalCoroutinesApi
    fun loginStateFlow() = callbackFlow {
        val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { p0, p1 ->
            val token = App.sharedPref?.getToken()
            trySend(token != null && token.isNotEmpty())
        }

        App.sharedPref?.registerOnSharedPreferenceChangeListener(changeListener)
        awaitClose {
            App.sharedPref?.unregisterOnSharedPreferenceChangeListener(changeListener)
        }
    }

    fun logout() = viewModelScope.launch {
        _authUiState.value = AuthUiState.Loading
        _authUiState.value = authRepository.logout().also {
            if (it is AuthUiState.Success && (it.data as LogoutMutation.Data).logout.success) {
                //clear token
                App.sharedPref?.edit()?.setToken(null)
            }
        }
    }

    init {
        getActiveOrder()
    }
}

// Represents different states for the LatestNews screen
sealed class CartUiState {
    data class Success(val items: AddItemToOrderMutation.AddItemToOrder) : CartUiState()
    data class Error(val exception: Throwable) : CartUiState()
    object Loading : CartUiState()
}