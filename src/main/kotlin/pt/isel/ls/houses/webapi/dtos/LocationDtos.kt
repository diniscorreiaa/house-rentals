package pt.isel.ls.houses.webapi.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.houses.domain.LocationType

/////////// Input ///////////
@Serializable
data class CreateLocationIn(val name: String, val type: String, val parentId: Int?)


/////////// Output ///////////
@Serializable
data class CreateLocationOut(val lid: Int)

@Serializable
data class GetLocationOut(val lid: Int, val name: String, val type: LocationType)