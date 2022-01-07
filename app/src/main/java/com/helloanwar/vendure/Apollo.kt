package com.helloanwar.vendure

import com.apollographql.apollo3.ApolloClient

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://vendure.helloanwar.com/shop-api")
    .build()