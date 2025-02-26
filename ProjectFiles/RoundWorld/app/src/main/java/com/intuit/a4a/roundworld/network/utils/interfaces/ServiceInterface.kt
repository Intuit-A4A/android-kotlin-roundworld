package com.intuit.a4a.roundworld.network.utils.interfaces

import com.intuit.a4a.roundworld.data.CountryRegion
import com.intuit.a4a.roundworld.data.CountryResponse

interface ServiceInterface {

    /**
     * Service API interface function to be implemented by the service layer for fetching country list
     * @param filterOption: Country region filter user selected from the dropdown
     * @param searchQuery: search query to be passed to the query string. This will be the search
     * query user type on the list top search bar.
     * @return: Return list of countries where each is represented by [CountryResponse]
     */
    suspend fun fetchCountryList(
        filterOption: CountryRegion,
        searchQuery: String? = null,
    ): List<CountryResponse>

    /**
     * Service API interface function to be implemented by the service layer for fetching individual country info
     * @param countryName: Common name of the country
     * @return: Return country info represented by [CountryResponse]
     */
    suspend fun getCountryInfo(countryName: String): CountryResponse
}
