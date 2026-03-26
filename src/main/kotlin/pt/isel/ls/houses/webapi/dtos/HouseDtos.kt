package pt.isel.ls.houses.webapi.dtos

import pt.isel.ls.houses.domain.Location

/////////// Input ///////////

data class CreateHouseIn(val title: String, val location: Int, val area: Double, val price: Double, val description: String)

/////////// Output ///////////

data class CreateHouseOut(val hid: Int)

data class GetHouseOut(val title: String, val location: Int, val area: Double, val price: Double, val description: String)