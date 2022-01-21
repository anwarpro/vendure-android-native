package com.helloanwar.vendure.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helloanwar.vendure.ActiveOrderQuery
import com.helloanwar.vendure.R
import com.helloanwar.vendure.ui.theme.VendureTheme

@ExperimentalMaterialApi
@Composable
fun DrawerContent(
    activeOrder: ActiveOrderQuery.ActiveOrder? = null,
    onCart: () -> Unit,
    onLogin: () -> Unit,
    onLogout: () -> Unit,
    loginState: State<Boolean>
) {
    Column(Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.wordmark_logo),
                contentDescription = "",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .height(100.dp)
                    .padding(vertical = 24.dp, horizontal = 16.dp)
            )
        }
        Divider()
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCart()
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "")
                Text(text = "Cart", modifier = Modifier.weight(1f))
                activeOrder?.let {
                    Text(
                        text = "${it.totalQuantity}",
                        modifier = Modifier
                            .background(MaterialTheme.colors.error, CircleShape)
                            .padding(vertical = 1.dp, horizontal = 6.dp),
                        color = Color.White
                    )
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {

                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(imageVector = Icons.Default.List, contentDescription = "")
                Text(text = "Orders", modifier = Modifier.weight(1f))
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {

                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(imageVector = Icons.Default.ListAlt, contentDescription = "")
                Text(text = "Terms & conditions", modifier = Modifier.weight(1f))
            }

            if (loginState.value) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onLogout()
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "")
                    Text(text = "Logout", modifier = Modifier.weight(1f))
                }
            } else {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onLogin()
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Login, contentDescription = "")
                    Text(text = "Login", modifier = Modifier.weight(1f))
                }
            }


        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun DrawerContentPreview() {
    val loginState = remember { mutableStateOf(false) }
    VendureTheme {
        DrawerContent(
            onCart = { /*TODO*/ },
            onLogin = { /*TODO*/ },
            loginState = loginState,
            onLogout = {})
    }
}