package com.intuit.a4a.roundworld.data

/**
 * Enum class representing the region parameter passed to the service API
 */
enum class CountryRegion(val region: String) {
    ALL("All"),
    AFRICA("Africa"),
    ASIA("Asia"),
    EUROPE("Europe"),
    AMERICAS("Americas"),
    OCEANIA("Oceania"),
    SOUTH_AMERICA("South America")
}