package com.intuit.a4a.roundworld.data

import com.google.gson.annotations.SerializedName

/**
 * Data class representing network response from https://restcountries.com/#endpoints-code
 */

data class CountryResponse(

    @SerializedName("name") var name: Name? = Name(),
    @SerializedName("capital") var capital: ArrayList<String> = arrayListOf(),
    @SerializedName("region") var region: String? = null,
    @SerializedName("latlng") var latlng: ArrayList<Double> = arrayListOf(),
    @SerializedName("population") var population: Int? = null,
    @SerializedName("continents") var continents: ArrayList<String> = arrayListOf(),
    @SerializedName("flags") var flags: Flags? = Flags(),
)

data class Flags(
    @SerializedName("png") var png: String? = null,
    @SerializedName("svg") var svg: String? = null
)

data class Name(
    @SerializedName("common") var common: String? = null,
    @SerializedName("official") var official: String? = null,
)
