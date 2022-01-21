package com.helloanwar.vendure

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInfo
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.github.ajalt.timberkt.d
import com.helloanwar.vendure.util.getToken
import com.helloanwar.vendure.util.setToken


val apolloClient = ApolloClient.Builder()
//    .serverUrl("https://vendure.helloanwar.com/shop-api")
    .serverUrl("http://103.146.2.175:3000/shop-api")
    .addHttpInterceptor(AuthorizationInterceptor())
    .addHttpInterceptor(LoggingInterceptor())
    .build()

class AuthorizationInterceptor() : HttpInterceptor {
    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {
        val token = App.sharedPref?.getToken() ?: ""
        d {
            "authToken sent => $token"
        }

        val httpResponse = chain.proceed(request)

        val vendureToken = httpResponse.headers.firstOrNull { it.name == "vendure-auth-token" }

        d {
            "authToken received => ${vendureToken?.name}"
        }

        return chain.proceed(
            request.newBuilder().also {
                if (token.isNotBlank()) {
                    it.addHeader("Authorization", "Bearer $token")
                }
            }.build()
        )
    }
}

fun <D : Operation.Data> ApolloResponse<D>.setLoginToken() {
    val httpContext = executionContext[HttpInfo]
    val token = httpContext?.headers?.firstOrNull { it.name == "vendure-auth-token" }
    token?.let {
        if (App.sharedPref?.getToken().isNullOrEmpty()) {
            App.sharedPref?.edit()?.setToken(it.value)
        }
    }
}