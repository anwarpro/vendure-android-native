package com.helloanwar.vendure.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.helloanwar.vendure.VendureScreen
import com.helloanwar.vendure.ui.auth.LoginScreen
import com.helloanwar.vendure.ui.auth.SignupScreen
import com.helloanwar.vendure.ui.cart.CartScreen
import com.helloanwar.vendure.ui.details.ProductDetailBody
import com.helloanwar.vendure.ui.home.HomeBody
import com.helloanwar.vendure.ui.splash.SplashBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun VendureNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    NavHost(
        navController = navController,
        startDestination = VendureScreen.HomeScreen.name,
        modifier = modifier
    ) {
        composable(VendureScreen.SplashScreen.name) {
            SplashBody()
        }
        composable(VendureScreen.HomeScreen.name) {
            HomeBody(
                onProductDetails = {
                    navController.navigate(VendureScreen.ProductDetail.name)
                }
            ) {

            }
        }
        composable(VendureScreen.ProductDetail.name) {
            ProductDetailBody()
        }
        composable(VendureScreen.CartScreen.name) {
            CartScreen()
        }
        composable(VendureScreen.LoginScreen.name) {
            LoginScreen(
                navController = navController,
                authViewModel = hiltViewModel(),
                scaffoldState = scaffoldState
            )
        }
        composable(VendureScreen.SignUpScreen.name) {
            SignupScreen(
                navController = navController,
                authViewModel = hiltViewModel(),
                scaffoldState = scaffoldState
            )
        }
    }
}