package pt.isel.ls.houses.domain

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val idLocation: Int, //unique identifier for the location
    val name: String,
    val type: LocationType,
    val parentLocation: Int?
)