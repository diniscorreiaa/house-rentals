package pt.isel.ls.houses.domain

import kotlinx.serialization.Serializable

@Serializable
data class House(
    val hid: Int,
    val title: String,
    val location: Int,
    val area: Double,
    val price: Double,
    val description: String,
    val owner: Int
)