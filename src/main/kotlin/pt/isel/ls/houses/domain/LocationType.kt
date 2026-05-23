package pt.isel.ls.houses.domain

import kotlinx.serialization.Serializable


enum class LocationType {
    COUNTRY,
    REGION,
    DISTRICT,
    MUNICIPALITY,
    LOCALITY
}