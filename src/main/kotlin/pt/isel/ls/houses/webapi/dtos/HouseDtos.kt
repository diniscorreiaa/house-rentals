package pt.isel.ls.houses.webapi.dtos

import kotlinx.serialization.Serializable
import pt.isel.ls.houses.domain.Location

/////////// Input ///////////
@Serializable
data class CreateHouseIn(val title: String, val location: Int, val area: Double, val price: Double, val description: String)


/////////// Output ///////////
@Serializable
data class CreateHouseOut(val hid: Int)

@Serializable
data class GetHouseOut(val title: String, val location: Int, val area: Double, val price: Double, val description: String)