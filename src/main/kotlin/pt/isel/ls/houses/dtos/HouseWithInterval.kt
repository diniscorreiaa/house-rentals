package pt.isel.ls.houses.dtos

import kotlinx.serialization.Serializable

@Serializable
data class HouseWithInterval(
    val hid: Int,
    val title: String,
    val location: Int,
    val area: Double,
    val price: Double,
    val description: String,
    val owner: Int,
    val reservedDays: Int
)