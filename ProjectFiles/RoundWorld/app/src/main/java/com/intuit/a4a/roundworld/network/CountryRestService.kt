package com.intuit.a4a.roundworld.network

import com.intuit.a4a.roundworld.data.CountryRegion
import com.intuit.a4a.roundworld.data.CountryResponse
import com.intuit.a4a.roundworld.network.utils.RestService
import okhttp3.Interceptor
import retrofit2.Converter

/**
 * Implementation of [RestService] interface for performing network request and parsing responses
 * with https://restcountries.com
 * Available endpoints are at https://restcountries.com/
 * The actual retrofit API interface is [CountryAPI]
 */
class CountryRestService: RestService<CountryAPI>(
    api = CountryAPI::class.java,
) {
    override fun getBaseUrl(): String = "https://restcountries.com"

    override fun getAdditionalConverters(): List<Converter.Factory> = emptyList()

    override fun getInterceptors(): List<Interceptor> = emptyList()

    override suspend fun fetchCountryList(
        filterOption: CountryRegion,
        searchQuery: String?
    ): List<CountryResponse> {
        val response =
            if (filterOption == CountryRegion.ALL) {
                apiInstance.queryCountries()
            } else {
                apiInstance.queryCountriesByRegion(region = filterOption.region)
            }

        return response.await().toList()
    }

    // Service layer, to be implemented as part of the detail page to get country info based on Country name
    override suspend fun getCountryInfo(countryName: String): CountryResponse = CountryResponse()
}
