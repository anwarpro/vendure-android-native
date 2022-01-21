package com.helloanwar.vendure.data.source.remote

import com.apollographql.apollo3.api.ApolloResponse
import com.helloanwar.vendure.CollectionListQuery
import com.helloanwar.vendure.ProductListQuery
import com.helloanwar.vendure.apolloClient
import com.helloanwar.vendure.ui.home.pageSize
import javax.inject.Inject

class ShopRemoteSource @Inject constructor() {
    suspend fun getProducts(page: Int): ApolloResponse<ProductListQuery.Data> {
        return apolloClient.query(ProductListQuery(pageSize, page * pageSize)).execute()
    }

    suspend fun getCollections(): ApolloResponse<CollectionListQuery.Data> {
        return apolloClient.query(CollectionListQuery(8, 0)).execute()
    }
}