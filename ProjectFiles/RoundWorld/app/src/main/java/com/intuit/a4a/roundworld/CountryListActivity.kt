package com.intuit.a4a.roundworld

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.intuit.a4a.roundworld.data.CountryRegion
import com.intuit.a4a.roundworld.data.CountryResponse
import com.intuit.a4a.roundworld.data.UiState
import com.intuit.a4a.roundworld.ui.components.CountryListRowComponent
import com.intuit.a4a.roundworld.ui.components.RegionDropDown
import kotlinx.coroutines.launch

class CountryListActivity : AppCompatActivity() {

    private val viewModel by viewModels<CountryListViewModel> {
        CountryListViewModelFactory(
            repository = CountryRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val countryList by viewModel.currentCountryListContentState.collectAsStateWithLifecycle()

            when (val state = countryList) {
                is UiState.Success -> {
                    CountryListUI(
                        countryList = state.data
                    )
                }
                else -> {}
            }
        }
    }

    /**
     * Compose function representing the UI to render when the response from the repository layer is successful
     * which includes a drop down menu for region selection and a list of country
     */
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun CountryListUI(
        countryList: List<CountryResponse>,
    ) {
        Scaffold (
            content = { padding ->
                val refreshScope = rememberCoroutineScope()
                // mutable boolean state indicating if pull to refresh is occurring
                var isRefreshing by rememberSaveable { mutableStateOf(false) }

                fun pullToRefresh() = refreshScope.launch {
                    isRefreshing = true
                    viewModel.fetchCountryList()
                    isRefreshing = false
                }

                val pullToRefreshState = rememberPullRefreshState(
                    refreshing = isRefreshing,
                    onRefresh = ::pullToRefresh
                )

                Column(modifier = Modifier.padding(padding)) {
                    RegionDropDown(
                        regionList = enumValues<CountryRegion>().toList(),
                        onItemClicked = { region ->
                            viewModel.onFilterOptionChanged(region)
                        }
                    )
                    Box(
                        Modifier
                            .pullRefresh(pullToRefreshState)
                    ) {
                        LazyColumn(
                            modifier = Modifier.testTag(LIST_TEST_TAG),
                        ) {
                            items(countryList) { country ->
                                CountryListRowComponent(
                                    item = country,
                                )
                            }
                        }
                        PullRefreshIndicator(
                            refreshing = isRefreshing,
                            state = pullToRefreshState,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }
                }
            }
        )

    }

    companion object {
        const val LIST_TEST_TAG = "countryList"
    }
}
