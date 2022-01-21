package com.helloanwar.vendure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.ajalt.timberkt.d
import com.helloanwar.vendure.ui.auth.components.BackButton
import com.helloanwar.vendure.ui.home.HomeScreenUiState
import com.helloanwar.vendure.ui.navigation.DrawerContent
import com.helloanwar.vendure.ui.navigation.VendureAppBar
import com.helloanwar.vendure.ui.navigation.VendureNavHost
import com.helloanwar.vendure.ui.theme.VendureTheme
import com.helloanwar.vendure.util.getToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalCoroutinesApi
    @ExperimentalFoundationApi
    @OptIn(InternalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VendureTheme {
                VendureApp()
            }
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@InternalCoroutinesApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VendureApp() {
    val allScreens = VendureScreen.values().toList()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = VendureScreen.fromRoute(backstackEntry.value?.destination?.route)

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val mainVm: MainActivityViewModel = hiltViewModel()

    val cart = mainVm.activeOrder.observeAsState()

    val token = App.sharedPref?.getToken()

    val loginState =
        mainVm.loginStateFlow().collectAsState(initial = token != null && token.isNotEmpty())

    val activeOrder = cart.value?.let {
        return@let when (it) {
            is HomeScreenUiState.Error -> {
                null
            }
            HomeScreenUiState.Loading -> null
            is HomeScreenUiState.Success -> (it.items as ActiveOrderQuery.Data).activeOrder
        }
    }

    activeOrder?.let {
        d { "orderActive => ${it.totalQuantity}" }
    }

    Scaffold(
        topBar = {
            when (currentScreen) {
                VendureScreen.SplashScreen -> {

                }
                VendureScreen.ProductDetail -> {

                }
                VendureScreen.CartScreen -> {

                }
                VendureScreen.LoginScreen, VendureScreen.SignUpScreen -> {
                    BackButton(R.drawable.ic_round_arrow_back, null) {
                        navController.navigateUp()
                    }
                }
                VendureScreen.HomeScreen -> {
                    VendureAppBar(scaffoldState, scope, activeOrder, navController)
                }
            }
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            DrawerContent(
                loginState = loginState,
                activeOrder = activeOrder,
                onCart = {
                    if (scaffoldState.drawerState.isOpen) {
                        scope.launch {
                            scaffoldState.drawerState.close()
                            navController.navigate(VendureScreen.CartScreen.name)
                        }
                    }
                },
                onLogin = {
                    if (scaffoldState.drawerState.isOpen) {
                        scope.launch {
                            scaffoldState.drawerState.close()
                            navController.navigate(VendureScreen.LoginScreen.name)
                        }
                    }
                },
                onLogout = {
                    //logout
                    mainVm.logout()
                }
            )
        },
        drawerGesturesEnabled = currentScreen == VendureScreen.HomeScreen
    ) { innerPadding ->
        VendureNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            scope = scope,
            scaffoldState = scaffoldState
        )
    }
}


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VendureTheme {
        VendureApp()
    }
}