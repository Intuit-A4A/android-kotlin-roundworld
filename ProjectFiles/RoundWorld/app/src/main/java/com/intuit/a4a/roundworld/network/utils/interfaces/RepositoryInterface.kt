package com.intuit.a4a.roundworld.network.utils.interfaces

import com.intuit.a4a.roundworld.data.CountryRegion
import com.intuit.a4a.roundworld.data.CountryResponse
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {

    fun getCountryList(
        filterOption: CountryRegion,
        searchQuery: String? = null,
    ): Flow<List<CountryResponse>>

    fun getCountryByName(
        countryName: String
    ): Flow<CountryResponse>
}
