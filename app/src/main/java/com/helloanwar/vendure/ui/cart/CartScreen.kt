package com.helloanwar.vendure.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.helloanwar.vendure.ActiveOrderQuery
import com.helloanwar.vendure.MainActivityViewModel
import com.helloanwar.vendure.R
import com.helloanwar.vendure.ui.home.HomeScreenUiState
import com.helloanwar.vendure.ui.theme.VendureTheme

@Composable
fun CartScreen(
    mainVm: MainActivityViewModel = hiltViewModel()
) {

    val order by mainVm.activeOrder.observeAsState()

    LazyColumn(Modifier.fillMaxSize()) {
        when (order) {
            is HomeScreenUiState.Error -> {
                item { Text(text = "Error") }
            }
            HomeScreenUiState.Loading -> {
                item { Text(text = "Loading") }
            }
            is HomeScreenUiState.Success -> {
                ((order as HomeScreenUiState.Success).items as ActiveOrderQuery.Data).activeOrder?.let {
                    items(it.lines) { line ->
                        CartItem(line)
                    }
                    if (it.lines.isNotEmpty()) {
                        item {
                            //show total
                            Column {
                                Text(text = "total ${it.total}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItem(line: ActiveOrderQuery.Line) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.wordmark_logo),
            contentDescription = "",
            modifier = Modifier.size(100.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "${line.quantity}")
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(text = "112")
                    Text(text = "125")
                }
                Row {
                    Icon(imageVector = Icons.Default.RemoveCircle, contentDescription = "")
                    BasicTextField(value = "${line.quantity}", onValueChange = {})
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "")
                }
            }
        }
    }
}

@Preview
@Composable
fun CartItemPreview() {
    VendureTheme {

    }
}