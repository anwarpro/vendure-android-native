package com.helloanwar.vendure.data.source.remote

import android.annotation.SuppressLint
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.network.http.HttpInfo
import com.helloanwar.vendure.*
import com.helloanwar.vendure.util.getToken
import com.helloanwar.vendure.util.setToken
import timber.log.Timber
import javax.inject.Inject

class CartRemoteSource @Inject constructor() {
    @SuppressLint("CommitPrefEdits")
    suspend fun addItemToOrder(variantId: String, qty: Int): CartUiState {
        val addItemToOrderMutation =
            AddItemToOrderMutation(productVariantId = variantId, quantity = qty)
        return try {
            val response = apolloClient.mutation(addItemToOrderMutation).execute()

            val httpContext = response.executionContext[HttpInfo]
            val token = httpContext?.headers?.firstOrNull { it.name == "vendure-auth-token" }
            token?.let {
                if (App.sharedPref?.getToken().isNullOrEmpty()) {
                    App.sharedPref?.edit()?.setToken(it.value)
                }
            }

            if (response.hasErrors() || response.data == null) {
                Timber.d("${response.errors}")
                CartUiState.Error(Throwable("Error"))
            } else {
                Timber.d("adding to cart=> ${response.data!!.addItemToOrder.onOrder}")
                CartUiState.Success(response.data!!.addItemToOrder)
            }
        } catch (e: ApolloException) {
            Timber.e(e)
            CartUiState.Error(e)
        }
    }

    suspend fun getActiveOrder(): ApolloResponse<ActiveOrderQuery.Data> {
        return apolloClient.query(ActiveOrderQuery()).execute()
    }
}