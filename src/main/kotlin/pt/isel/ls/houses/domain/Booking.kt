package pt.isel.ls.houses.domain

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val bid: Int, // unique identifier for the booking
    val startDate: String,
    val endDate: String,
    val user: Int,
    val house: Int
)