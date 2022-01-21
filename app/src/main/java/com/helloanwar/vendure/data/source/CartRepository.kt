package com.helloanwar.vendure.data.source

import com.helloanwar.vendure.CartUiState
import com.helloanwar.vendure.data.source.remote.CartRemoteSource
import com.helloanwar.vendure.ui.home.HomeScreenUiState
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val remoteSource: CartRemoteSource
) {
    suspend fun addItemToCart(variantId: String, qty: Int): CartUiState {
        return remoteSource.addItemToOrder(variantId, qty)
    }

    suspend fun getActiveOrder(): HomeScreenUiState {
        val response = remoteSource.getActiveOrder()
        if (response.hasErrors() || response.data == null) {
            return HomeScreenUiState.Error(Throwable("Error"))
        }
        return HomeScreenUiState.Success(response.data!!)
    }
}