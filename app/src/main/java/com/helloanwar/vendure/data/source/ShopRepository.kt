package com.helloanwar.vendure.data.source

import android.annotation.SuppressLint
import com.apollographql.apollo3.network.http.HttpInfo
import com.github.ajalt.timberkt.d
import com.helloanwar.vendure.App
import com.helloanwar.vendure.ProductListQuery
import com.helloanwar.vendure.data.source.remote.ShopRemoteSource
import com.helloanwar.vendure.ui.home.HomeScreenUiState
import com.helloanwar.vendure.util.setToken
import javax.inject.Inject

class ShopRepository @Inject constructor(
    private val remoteSource: ShopRemoteSource
) {
    @SuppressLint("CommitPrefEdits")
    suspend fun getProducts(page: Int): ProductListQuery.Products? {
        val response = remoteSource.getProducts(page)
        if (response.hasErrors() || response.data == null) {
            return null
        }

        val httpContext = response.executionContext[HttpInfo]
        httpContext?.headers?.forEach {
            d { "${it.name} => ${it.value}" }
        }

        val token = httpContext?.headers?.firstOrNull { it.name == "vendure-auth-token" }
        token?.let {
            App.sharedPref?.edit()?.setToken(it.value)
            d { "authToken => ${it.value}" }
        }

        return response.data?.products
    }

    suspend fun getCollections(): HomeScreenUiState {
        val response = remoteSource.getCollections()
        if (response.hasErrors() || response.data == null) {
            return HomeScreenUiState.Error(Throwable("Error"))
        }
        return HomeScreenUiState.Success(response.data!!)
    }
}