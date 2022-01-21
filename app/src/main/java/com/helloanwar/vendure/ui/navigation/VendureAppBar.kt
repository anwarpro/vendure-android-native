package com.helloanwar.vendure.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.helloanwar.vendure.ActiveOrderQuery
import com.helloanwar.vendure.R
import com.helloanwar.vendure.VendureScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Composable
fun VendureAppBar(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    activeOrder: ActiveOrderQuery.ActiveOrder?,
    navController: NavHostController
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = {
                if (scaffoldState.drawerState.isClosed) {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "")
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .clickable {

                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = ""
                )
            }

            if (activeOrder == null) {
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .clickable {

                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = ""
                    )
                }
            } else {
                activeOrder.let {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .clickable {
                                navController.navigate(VendureScreen.CartScreen.name)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        BadgeBox(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }
    )
}