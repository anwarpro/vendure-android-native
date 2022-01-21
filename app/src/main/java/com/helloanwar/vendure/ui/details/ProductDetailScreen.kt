package com.helloanwar.vendure.ui.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helloanwar.vendure.ui.theme.VendureTheme
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ProductDetailBody() {
    Column {
        Image(
            imageVector = Icons.Default.CardTravel,
            contentDescription = null,
            modifier = Modifier.height(250.dp)
        )
        Text(text = "Masuri Dal 2no")
        Row {
            for (rating in 1..5) {
                Icon(imageVector = Icons.Default.StarRate, contentDescription = null)
            }
            Text(text = "0.0 (0 Reviews)")
        }
        Row {
            Column {
                Row {
                    Text(text = "TK. 47.0")
                    Text(text = "(TK. 50.0)")
                }
                Text(text = "6% off")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Add to cart")
            }
        }
        Column {
            Text(text = "Available size")
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                }
                Text(text = "500 gm")
            }
        }
        Text(text = "Related products")
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview
@Composable
fun DefaultPreview() {
    VendureTheme {
        ProductDetailBody()
    }
}