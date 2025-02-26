package com.intuit.a4a.roundworld.network

import com.intuit.a4a.roundworld.data.CountryRegion
import com.intuit.a4a.roundworld.data.CountryResponse
import com.intuit.a4a.roundworld.network.utils.GraphQLService
import okhttp3.Interceptor

class CountryGraphQlService: GraphQLService() {
    override fun getBaseUrl(): String = "https://graphql.country/graphql"

    override fun getInterceptors(): List<Interceptor> = emptyList()

    override suspend fun fetchCountryList(
        filterOption: CountryRegion,
        searchQuery: String?
    ): List<CountryResponse> = emptyList()

    override suspend fun getCountryInfo(countryName: String): CountryResponse {
        return CountryResponse()
    }
}
