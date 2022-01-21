package com.helloanwar.vendure

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector

enum class VendureScreen(
    val icon: ImageVector
) {
    SplashScreen(
        icon = Icons.Filled.PieChart,
    ),
    ProductDetail(
        icon = Icons.Filled.PieChart
    ),
    CartScreen(
        icon = Icons.Filled.ArrowBack
    ),
    LoginScreen(
        icon = Icons.Filled.ArrowBack
    ),
    SignUpScreen(
        icon = Icons.Filled.ArrowBack
    ),
    HomeScreen(
        icon = Icons.Filled.Home
    );

    companion object {
        fun fromRoute(route: String?): VendureScreen =
            when (route?.substringBefore("/")) {
                SplashScreen.name -> SplashScreen
                HomeScreen.name -> HomeScreen
                ProductDetail.name -> ProductDetail
                CartScreen.name -> CartScreen
                LoginScreen.name -> LoginScreen
                SignUpScreen.name -> SignUpScreen
                null -> HomeScreen
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}