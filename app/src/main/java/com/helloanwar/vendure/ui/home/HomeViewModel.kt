package com.helloanwar.vendure.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.apollographql.apollo3.api.Query
import com.helloanwar.vendure.data.source.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {
    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val _collections = MutableLiveData<HomeScreenUiState>(HomeScreenUiState.Loading)
    val collections: LiveData<HomeScreenUiState>
        get() = _collections

    fun getCollections() = viewModelScope.launch {
        _collections.value = HomeScreenUiState.Loading
        _collections.value = shopRepository.getCollections()
    }

    val pager = Pager(
        PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = true,
            maxSize = 200
        )
    ) { ProductPagingSource(shopRepository) }

    init {
        getCollections()
    }
}

// Represents different states for the LatestNews screen
sealed class HomeScreenUiState {
    data class Success(val items: Query.Data) : HomeScreenUiState()
    data class Error(val exception: Throwable) : HomeScreenUiState()
    object Loading : HomeScreenUiState()
}