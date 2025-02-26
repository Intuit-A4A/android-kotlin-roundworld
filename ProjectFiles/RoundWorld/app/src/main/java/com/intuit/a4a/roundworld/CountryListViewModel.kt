package com.intuit.a4a.roundworld

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.intuit.a4a.roundworld.data.CountryRegion
import com.intuit.a4a.roundworld.data.CountryResponse
import com.intuit.a4a.roundworld.data.UiState
import com.intuit.a4a.roundworld.network.utils.interfaces.RepositoryInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CountryListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: RepositoryInterface,
) : ViewModel() {

    companion object {
        private const val TAG = "CountryListViewModel"
        private const val CURRENT_COUNTRY_LIST_KEY = "currentCountryList"
        private const val CURRENT_REGION_FILTER_KEY = "currentRegionFilter"
    }

    // state representing the country list response from fetchCountryList
    private val _currentCountryListContentState = MutableStateFlow<UiState<List<CountryResponse>>>(
        savedStateHandle[CURRENT_COUNTRY_LIST_KEY] ?: UiState.Loading
    )
    val currentCountryListContentState: StateFlow<UiState<List<CountryResponse>>> = _currentCountryListContentState.asStateFlow()

    // state representing the current region dropdown selection
    private val _currentRegionSelection = MutableStateFlow(
        savedStateHandle[CURRENT_REGION_FILTER_KEY] ?: CountryRegion.ALL
    )

    private val listExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _currentCountryListContentState.value = UiState.Error(throwable)
        Log.e(TAG, "Error when fetching country list,  ${throwable.message}")
    }

    init {
        fetchCountryList()
    }

    /**
     * VM function to be triggered when the dropdown menu filter option is changed
     */
    fun onFilterOptionChanged(filterOption: CountryRegion) {
        savedStateHandle[CURRENT_REGION_FILTER_KEY] = filterOption
        _currentRegionSelection.value = filterOption
    }

    /**
     * Function to fetch country list from the repository layer.
     */
    fun fetchCountryList() {
        viewModelScope.launch(Dispatchers.IO + listExceptionHandler) {
            _currentRegionSelection.collectLatest { region ->
                repository.getCountryList(region).collect { countryList ->
                    _currentCountryListContentState.value = UiState.Success(countryList)
                }
            }
        }
    }
}

internal class CountryListViewModelFactory(
    private val repository: RepositoryInterface
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        CountryListViewModel(
            extras.createSavedStateHandle(),
            repository
        ) as T
}
