package com.intuit.a4a.roundworld

import com.intuit.a4a.roundworld.data.CountryRegion
import com.intuit.a4a.roundworld.data.CountryResponse
import com.intuit.a4a.roundworld.network.CountryRestService
import com.intuit.a4a.roundworld.network.utils.interfaces.RepositoryInterface
import com.intuit.a4a.roundworld.network.utils.interfaces.ServiceInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountryRepository(
    private val countryService: ServiceInterface = CountryRestService()
): RepositoryInterface {
    override fun getCountryList(filterOption: CountryRegion, searchQuery: String?): Flow<List<CountryResponse>> =
        flow { emit(countryService.fetchCountryList(filterOption, searchQuery)) }

    // Repository layer, to be implemented as part of the detail page to get country info based on Country name
    override fun getCountryByName(countryName: String): Flow<CountryResponse> =
        flow { emptyList<CountryResponse>() }
}
