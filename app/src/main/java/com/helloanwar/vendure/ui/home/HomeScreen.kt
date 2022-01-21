package com.helloanwar.vendure.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.github.ajalt.timberkt.Timber
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.helloanwar.vendure.ActiveOrderQuery
import com.helloanwar.vendure.CollectionListQuery
import com.helloanwar.vendure.MainActivityViewModel
import com.helloanwar.vendure.ProductListQuery
import com.helloanwar.vendure.ui.theme.Green200
import com.helloanwar.vendure.ui.theme.Green500
import com.helloanwar.vendure.ui.theme.VendureTheme
import com.helloanwar.vendure.ui.util.items
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun HomeBody(
    onProductDetails: () -> Unit,
    onCategoryClicked: () -> Unit
) {
    val vm: HomeViewModel = hiltViewModel()
    val lazyPagingProducts = vm.pager.flow.collectAsLazyPagingItems()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = lazyPagingProducts.loadState.refresh == LoadState.Loading),
        onRefresh = {
            lazyPagingProducts.refresh()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            PopularProducts(vm, lazyPagingProducts) { onProductDetails() }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CategoryItem(collections: List<CollectionListQuery.Item>) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        collections.forEach { collectionItem ->
            Card(
                onClick = {

                },
                modifier = Modifier
                    .weight(1f, true)
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberImagePainter(collectionItem.featuredAsset?.source),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.FillBounds
                    )
                    Text(text = collectionItem.name, maxLines = 1)
                }
            }
        }
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun PopularProducts(
    vm: HomeViewModel = hiltViewModel(),
    lazyPagingProducts: LazyPagingItems<ProductListQuery.Item>,
    onProductDetails: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {

        val collections = vm.collections.observeAsState()

        val mainVm: MainActivityViewModel = hiltViewModel()

        if (lazyPagingProducts.loadState.refresh == LoadState.Loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (collections.value is HomeScreenUiState.Success) {
                val allCollections =
                    ((collections.value as HomeScreenUiState.Success).items as CollectionListQuery.Data)
                        .collections.items

                item {
                    Text(
                        text = "Categories",
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp)
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "See All", color = Green500,
                            modifier = Modifier.clickable {

                            }
                        )
                    }
                }

                allCollections.chunked(4).forEachIndexed { index, _collections ->
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = if (index % 2 == 0) 16.dp else 0.dp,
                                    end = if (index % 2 == 1) 16.dp else 0.dp
                                )
                        ) {
                            _collections.chunked(2).forEach { _collection ->
                                CategoryItem(_collection)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Popular products",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                )
            }
            item {

            }

            items(lazyPagingProducts) { item ->
                item?.let {
                    ProductCard(item, mainVm) { onProductDetails() }
                }
            }

            if (lazyPagingProducts.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ProductCard(
    product: ProductListQuery.Item,
    mainVm: MainActivityViewModel,
    onProductClicked: () -> Unit
) {
    var loading by remember { mutableStateOf(true) }
    val cart = mainVm.activeOrder.observeAsState()

    val activeOrder = cart.value?.let {
        return@let when (it) {
            is HomeScreenUiState.Error -> {
                null
            }
            HomeScreenUiState.Loading -> null
            is HomeScreenUiState.Success -> (it.items as ActiveOrderQuery.Data).activeOrder
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .placeholder(
                visible = loading,
                color = Color.Gray,
                highlight = PlaceholderHighlight.shimmer()
            ),
        onClick = {
            onProductClicked()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberImagePainter(product.featuredAsset?.source),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "0.60 off",
                        modifier = Modifier
                            .background(
                                color = Green500,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .padding(2.dp),
                        color = Color.White
                    )
                }
            }
            Text(text = product.name, modifier = Modifier.padding(horizontal = 4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${product.variants.first().price}")
/*                Text(
                    text = "50",
                    style = TextStyle(textDecoration = TextDecoration.LineThrough)
                )*/
                Text(text = "${product.variants.first().name}")
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val isAdded = activeOrder?.let {
                    it.lines.firstOrNull { it.productVariant.productId == product.id }
                }
                if (isAdded == null) {
                    loading = false
                    Button(
                        onClick = {
                            //add to cart
                            product.variants.firstOrNull()?.let {
                                mainVm.addToCart(it.id, 1)
                            }
                            Timber.d { "adding" }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Green200
                        )
                    ) {
                        Text(text = "Add to cart")
                    }
                } else {
                    loading = false
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                //minus
                            }
                        ) {
                            Icon(imageVector = Icons.Outlined.Remove, contentDescription = null)
                        }
                        BasicTextField(
                            value = "${isAdded.quantity}",
                            onValueChange = {},
                            modifier = Modifier.width(20.dp)
                        )
                        IconButton(
                            onClick = {
                                //increment
                            }
                        ) {
                            Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                        }
                    }
                }
            }
        }
    }

}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview
@Composable
fun DefaultPreview() {
    VendureTheme {
        HomeBody(onProductDetails = {}) {}
    }
}