package com.helloanwar.vendure.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.helloanwar.vendure.ProductListQuery
import com.helloanwar.vendure.data.source.ShopRepository
import javax.inject.Inject

private const val STARTING_PAGE_INDEX = 0
const val pageSize = 10

class ProductPagingSource @Inject constructor(
    private val shopRepository: ShopRepository
) : PagingSource<Int, ProductListQuery.Item>() {

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, ProductListQuery.Item>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductListQuery.Item> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = shopRepository.getProducts(page)
            val products = response?.items ?: emptyList()
            val nextKey = if (products.isEmpty()) {
                null
            } else {
                page + 1
            }

            LoadResult.Page(
                data = products,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}