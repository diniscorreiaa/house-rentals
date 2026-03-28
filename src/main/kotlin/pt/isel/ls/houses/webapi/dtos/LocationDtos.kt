package pt.isel.ls.houses.webapi.dtos

import pt.isel.ls.houses.domain.LocationType

/////////// Input ///////////

data class CreateLocationIn(val name: String, val type: LocationType, val parentLocation: Int?)


/////////// Output ///////////

data class CreateLocationOut(val lid: Int)

data class GetLocationOut(val name: String, val type: LocationType)