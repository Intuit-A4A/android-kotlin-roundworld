package com.intuit.a4a.roundworld.network

import com.intuit.a4a.roundworld.data.CountryResponse
import com.intuit.a4a.roundworld.network.utils.ErrorHandlingCall
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Network service doc: https://restcountries.com/
 */
interface CountryAPI {
    // adds field query parameter to limit the size of the network response as the backend will have intermittent json syntax errors for response being too big
    // https://gitlab.com/restcountries/restcountries/-/issues/239
    @GET("/v3.1/all?fields=name,flags,capital")
    fun queryCountries(): ErrorHandlingCall<List<CountryResponse>>

    @GET("/v3.1/region/{region}")
    fun queryCountriesByRegion(
        @Path("region") region: String
    ): ErrorHandlingCall<List<CountryResponse>>
}
